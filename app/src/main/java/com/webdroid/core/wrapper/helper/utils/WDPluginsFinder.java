package com.webdroid.core.wrapper.helper.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.webdroid.core.wrapper.WDWebViewWrapper;
import com.webdroid.core.wrapper.helper.WDPluginHelper;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dalvik.system.DexFile;

public class WDPluginsFinder {
    public static final String TAG = WDPluginsFinder.class.getSimpleName();
    private Context context;
    private HashMap<String, MethodMeta> methodRegistry = new HashMap<>();
    private HashMap<String, HashMap<String, PluginMeta>> pluginClassRegistry = new HashMap<>();
    private WDWebViewWrapper webViewWrapper;

    public WDPluginsFinder(Context context, WDWebViewWrapper webInterface) {
        this.context = context;
        webViewWrapper = webInterface;
        findAllPlugins(context);
    }

    private void findAllPlugins(Context context) {
        HashMap<String, List<PluginMeta>> plugins = new HashMap<>();
        try {
            DexFile dexFile = new DexFile(context.getPackageCodePath());
            for (Enumeration<String> item = dexFile.entries(); item.hasMoreElements(); ) {
                String s = item.nextElement();
                String replacePlugin = WDPluginHelper.USER_PLUGINS_PATH;
                Class<?> pluginCls = null;
                if (s.startsWith(WDPluginHelper.USER_PLUGINS_PATH)) {
                    pluginCls = Class.forName(s);
                } else if (s.startsWith(WDPluginHelper.SYSTEM_PLUGINS_PATH)) {
                    pluginCls = Class.forName(s);
                    replacePlugin = WDPluginHelper.SYSTEM_PLUGINS_PATH;
                }
                if (pluginCls != null && pluginCls.getSuperclass() != null &&
                        pluginCls.getSuperclass() == WDPluginHelper.class) {
                    String pluginRoot = pluginCls.getPackage().getName()
                            .replace(replacePlugin + ".", "");
                    PluginMeta pluginMeta = new PluginMeta();
                    pluginMeta.setPluginAlias(pluginRoot);
                    pluginMeta.setPluginClass((Class<? extends WDPluginHelper>) pluginCls);
                    pluginMeta.setPluginMethods(getPluginMethods(pluginRoot, pluginMeta.getPluginClass()));
                    HashMap<String, PluginMeta> pluginMetaHashMap = new HashMap<>();
                    if (plugins.containsKey(pluginRoot)) {
                        pluginMetaHashMap.putAll(pluginClassRegistry.get(pluginRoot));
                    }
                    pluginMetaHashMap.put(pluginMeta.getPluginClass().getName(), pluginMeta);
                    pluginClassRegistry.put(pluginRoot, pluginMetaHashMap);
                }
            }

            Log.v(TAG, TextUtils.join(", ", pluginClassRegistry.keySet()) + " Plugins registered");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllPluginsAlias() {
        List<String> plugins = new ArrayList<>();
        plugins.addAll(pluginClassRegistry.keySet());
        return plugins;
    }

    private HashMap<String, Method> getPluginMethods(String alias, Class<? extends WDPluginHelper> plugin) {
        HashMap<String, Method> methods = new HashMap<>();
        for (Method method : plugin.getDeclaredMethods()) {
            method.setAccessible(true);
            Class<?>[] params = method.getParameterTypes();
            if (params.length > 0 && params[0].getName()
                    .equals(WDPluginArgs.class.getName())) {
                methods.put(method.getName(), method);
                MethodMeta methodMeta = new MethodMeta();
                methodMeta.setPluginAlias(alias);
                methodMeta.setMethodName(method.getName());
                methodMeta.setPluginClassName(plugin.getName());
                methodRegistry.put(alias + "_" + method.getName(), methodMeta);
            }
        }

        return methods;
    }

    public List<String> getPluginMethods(String pluginName) {
        List<String> methods = new ArrayList<>();
        if (pluginClassRegistry.containsKey(pluginName)) {
            HashMap<String, PluginMeta> pluginData = pluginClassRegistry.get(pluginName);
            for (String key : pluginData.keySet()) {
                PluginMeta meta = pluginData.get(key);
                methods.addAll(meta.getPluginMethods().keySet());
            }
        }
        return methods;
    }

    public Object triggerAction(String alias, String action, String args, String callbackId) {
        if (methodRegistry.containsKey(alias + "_" + action)) {
            Log.v(TAG, "▶ triggering action");
            Log.v(TAG, "▶ ALIAS  : " + alias);
            Log.v(TAG, "▶ ACTION : " + action);
            MethodMeta methodMeta = methodRegistry.get(alias + "_" + action);
            try {
                Class<?> plugin = pluginClassRegistry.get(methodMeta.getPluginAlias()).get(methodMeta.getPluginClassName()).getPluginClass();
                Constructor<?> constructor = plugin.getConstructor(Context.class);
                WDPluginHelper pluginObj = (WDPluginHelper) constructor.newInstance(context);
                pluginObj.setWebView(webViewWrapper.getWebView());
                // Setting callback if any
                WDPluginCallback callback = null;
                if (callbackId != null) {
                    callback = new WDPluginCallback(context, webViewWrapper.getWebView(),
                            callbackId);
                }
                Method method;
                WDPluginArgs pluginArgs = dicToArgs(args);
                // Check for method callback actions.
                if (callback != null) {
                    method = plugin.getMethod(action, WDPluginArgs.class, WDPluginCallback.class);
                } else {
                    method = plugin.getMethod(action, WDPluginArgs.class);
                }
                if (method != null) {
                    if (!method.getReturnType().equals(Void.TYPE)) {
                        Log.v(TAG, "• returning value from method");
                        return method.invoke(pluginObj, pluginArgs);
                    } else {
                        if (callback != null) {
                            method.invoke(pluginObj, pluginArgs, callback);
                        } else {
                            method.invoke(pluginObj, pluginArgs);
                        }
                    }
                }
                Log.v(TAG, "• Action triggered successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.w(TAG, "ALIAS " + alias + " not found");
        }
        return "false";
    }

    private WDPluginArgs dicToArgs(String data) {
        WDPluginArgs args = new WDPluginArgs();
        try {
            JSONObject dic = new JSONObject(data);
            Iterator<String> keys = dic.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object obj = dic.get(key);
                if (obj instanceof JSONObject) {
                    args.put(key, dicToArgs(obj.toString()));
                } else {
                    args.put(key, dic.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return args;
    }

    public class MethodMeta {
        private String methodName;
        private String pluginClassName;
        private String pluginAlias;

        public String getPluginAlias() {
            return pluginAlias;
        }

        public void setPluginAlias(String pluginAlias) {
            this.pluginAlias = pluginAlias;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getPluginClassName() {
            return pluginClassName;
        }

        public void setPluginClassName(String pluginClassName) {
            this.pluginClassName = pluginClassName;
        }

        @Override
        public String toString() {
            return "MethodMeta{" +
                    "methodName='" + methodName + '\'' +
                    ", pluginClassName='" + pluginClassName + '\'' +
                    ", pluginAlias='" + pluginAlias + '\'' +
                    '}';
        }
    }

    public class PluginMeta {
        private Class<? extends WDPluginHelper> pluginClass;
        private HashMap<String, Method> pluginMethods = new HashMap<>();
        private String pluginAlias;

        public boolean containMethod(String method) {
            return pluginMethods.containsKey(method);
        }

        public String getPluginAlias() {
            return pluginAlias;
        }

        public void setPluginAlias(String pluginAlias) {
            this.pluginAlias = pluginAlias;
        }

        public Class<? extends WDPluginHelper> getPluginClass() {
            return pluginClass;
        }

        public void setPluginClass(Class<? extends WDPluginHelper> pluginClass) {
            this.pluginClass = pluginClass;
        }

        public HashMap<String, Method> getPluginMethods() {
            return pluginMethods;
        }

        public void setPluginMethods(HashMap<String, Method> pluginMethods) {
            this.pluginMethods = pluginMethods;
        }
    }

}
