package net.sourceforge.pmd.lang;

import net.sourceforge.pmd.lang.java.JavaLanguageModule;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by christoferdutz on 20.09.14.
 */
public class LanguageRegistry {

    private static LanguageRegistry instance;

    private Map<String, Language> languages;

    private LanguageRegistry() {
        languages = new HashMap<String, Language>();

        try {
            Enumeration<URL> languageModulesProperties = LanguageRegistry.class.getClassLoader().getResources("META-INF/pmd-language.properties");
            while (languageModulesProperties.hasMoreElements()) {
                URL curLanguageModulePropertiesUrl = languageModulesProperties.nextElement();
                Properties curLanguageModuleProperties = new Properties();
                curLanguageModuleProperties.load(curLanguageModulePropertiesUrl.openStream());
                String languageModulesImplClassNames = curLanguageModuleProperties.getProperty(Language.LANGUAGE_MODULES_CLASS_NAMES_PROPERTY);
                if(languageModulesImplClassNames != null) {
                    String[] languageModuleImplClassNames = languageModulesImplClassNames.split(";");
                    for(String languageModuleImplClassName : languageModuleImplClassNames) {
                        Class<?> languageModuleImplClass = LanguageRegistry.class.getClassLoader().loadClass(languageModuleImplClassName);
                        Language languageModule = (Language) languageModuleImplClass.newInstance();
                        languages.put(languageModule.getName(), languageModule);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected static LanguageRegistry getInstance() {
        if(instance == null) {
            instance = new LanguageRegistry();
        }
        return instance;
    }

    public static Collection<Language> getLanguages() {
        return getInstance().languages.values();
    }

    public static Language getLanguage(String languageName) {
        return getInstance().languages.get(languageName);
    }

    public static Language getDefaultLanguage() {
        return getLanguage(JavaLanguageModule.NAME);
    }

    public static Language findLanguageByTerseName(String terseName) {
        for (Language language : getInstance().languages.values()) {
            if (language.getTerseName().equals(terseName)) {
                return language;
            }
        }
        return null;
    }

    public static LanguageVersion findLanguageVersionByTerseName(String terseName) {
        String version = null;
        if(terseName.contains(" ")) {
            version = terseName.substring(terseName.lastIndexOf(" ") + 1);
            terseName = terseName.substring(0, terseName.lastIndexOf(" "));
        }
        Language language = findLanguageByTerseName(terseName);
        if(language != null) {
            if(version == null) {
                return language.getDefaultVersion();
            } else {
                return language.getVersion(version);
            }
        }
        return null;
    }

    public static List<Language> findByExtension(String extension) {
        List<Language> languages = new ArrayList<Language>();
        for (Language language : getInstance().languages.values()) {
            if (language.hasExtension(extension)) {
                languages.add(language);
            }
        }
        return languages;
    }

    public static List<LanguageVersion> findAllVersions() {
        List<LanguageVersion> versions = new ArrayList<LanguageVersion>();
        for(Language language : getLanguages()) {
            for(LanguageVersion languageVersion : language.getVersions()) {
                versions.add(languageVersion);
            }
        }
        return versions;
    }

    /**
     * A utility method to find the Languages which have Rule support.
     * @return A List of Languages with Rule support.
     */
    public static List<Language> findWithRuleSupport() {
        List<Language> languages = new ArrayList<Language>();
        for (Language language : getInstance().languages.values()) {
            if (language.getRuleChainVisitorClass() != null) {
                languages.add(language);
            }
        }
        return languages;
    }

    public static String commaSeparatedTerseNamesForLanguage(List<Language> languages) {
        StringBuilder builder = new StringBuilder();
        for (Language language : languages) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(language.getTerseName());
        }
        return builder.toString();
    }

    public static String commaSeparatedTerseNamesForLanguageVersion(List<LanguageVersion> languageVersions) {
        if (languageVersions == null || languageVersions.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(languageVersions.get(0).getTerseName());
        for (int i=1; i<languageVersions.size(); i++) {
            builder.append(", ").append(languageVersions.get(i).getTerseName());
        }
        return builder.toString();
    }

}
