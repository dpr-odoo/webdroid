/**
 * Odoo, Open Source Management Solution
 * Copyright (C) 2012-today Odoo SA (<http:www.odoo.com>)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * <p/>
 * Created on 1/7/15 3:52 PM
 */
package com.webdroid.core.utils;

import android.content.Context;

public class ORes {
    private Context mContext;

    public ORes(Context context) {
        mContext = context;
    }

    public static ORes get(Context context) {
        return new ORes(context);
    }

    public String _s(int resId) {
        return mContext.getString(resId);
    }

    public int _dim(int resId) {
        return (int) mContext.getResources().getDimension(resId);
    }

    public int _c(int resId) {
        return mContext.getResources().getColor(resId);
    }
}
