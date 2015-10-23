package com.plugins.database;

import com.plugins.database.tables.Achievements;
import com.plugins.database.tables.Additional;
import com.plugins.database.tables.Company;
import com.plugins.database.tables.Courses;
import com.plugins.database.tables.Languages;
import com.plugins.database.tables.PersonalDetail;
import com.plugins.database.tables.Projects;
import com.plugins.database.tables.Skills;
import com.plugins.database.tables.SystemUsers;

import java.util.ArrayList;
import java.util.List;

public class Tables {

    public List<Class<? extends DatabaseWrapper>> tables() {
        List<Class<? extends DatabaseWrapper>> tables = new ArrayList<>();
        tables.add(SystemUsers.class);
        tables.add(PersonalDetail.class);
        tables.add(Courses.class);
        tables.add(Company.class);
        tables.add(Achievements.class);
        tables.add(Projects.class);
        tables.add(Additional.class);
        tables.add(Languages.class);
        tables.add(Skills.class);
        return tables;
    }
}
