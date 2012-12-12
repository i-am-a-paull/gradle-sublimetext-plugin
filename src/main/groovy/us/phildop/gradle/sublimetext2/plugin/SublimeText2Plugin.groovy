package us.phildop.gradle.sublimetext2.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin

class SublimeText2Plugin implements Plugin<Project> {

  void apply(Project project) {
    def extension = new SublimeText2PluginExtension(project)
    project.extensions.sublimeText = extension

    project.tasks.add("sublimeText", SublimeText2Task)
    project.tasks.withType(SublimeText2Task) {
      conventionMapping.sublimeProjectName = {
        project.hasProperty("sublimeProjectName") ? project.property("sublimeProjectName")
                                                  : extension.sublimeProjectName
      }
      conventionMapping.defaultFileExcludePatterns = {
        project.hasProperty("defaultFileExcludePatterns") ? project.property("defaultFileExcludePatterns")
                                                          : extension.defaultFileExcludePatterns
      }
      conventionMapping.defaultFolderExcludePatterns = {
        project.hasProperty("defaultFolderExcludePatterns") ? project.property("defaultFolderExcludePatterns")
                                                            : extension.defaultFolderExcludePatterns
      }
      conventionMapping.addDependencyProjects = {
        project.hasProperty("addDependencyProjects") ? project.property("addDependencyProjects")
                                                     : extension.addDependencyProjects
      }
      conventionMapping.generateSublimeJavaClasspath = {
        project.hasProperty("generateSublimeJavaClasspath") ? project.property("generateSublimeJavaClasspath")
                                                            : extension.generateSublimeJavaClasspath
      }
      conventionMapping.generateSublimeJavaSrcpath = {
        project.hasProperty("generateSublimeJavaSrcpath") ? project.property("generateSublimeJavaSrcpath")
                                                            : extension.generateSublimeJavaSrcpath
      }
      conventionMapping.addGradleCompile = {
        project.hasProperty("addGradleCompile") ? project.property("addGradleCompile")
                                                : extension.addGradleCompile
      }
      conventionMapping.addSublimeLinterConfig = {
        project.hasProperty("addSublimeLinterConfig") ? project.property("addSublimeLinterConfig")
                                                            : extension.addSublimeLinterConfig
      }
      conventionMapping.eclipseJavaFormatterConfigFile = {
        project.hasProperty("eclipseJavaFormatterConfigFile") ? project.property("eclipseJavaFormatterConfigFile")
                                                            : extension.eclipseJavaFormatterConfigFile
      }
      conventionMapping.eclipseJavaFormatterSortImportsOrder = {
        project.hasProperty("eclipseJavaFormatterSortImportsOrder") ? project.property("eclipseJavaFormatterSortImportsOrder")
                                                            : extension.eclipseJavaFormatterSortImportsOrder
      }
      conventionMapping.eclipseJavaFormatterRestoreLineEndings = {
        project.hasProperty("eclipseJavaFormatterRestoreLineEndings") ? project.property("eclipseJavaFormatterRestoreLineEndings")
                                                            : extension.eclipseJavaFormatterRestoreLineEndings
      }
    }
  }
}
