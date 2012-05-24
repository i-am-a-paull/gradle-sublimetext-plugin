package us.phildop.gradle.sublimetext2.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import us.phildop.gradle.sublimetext2.SublimeProject

class SublimeText2Task extends DefaultTask {

  String sublimeProjectName
  List<String> defaultFileExcludePatterns
  List<String> defaultFolderExcludePatterns
  boolean addDependencyProjects
  boolean generateSublimeJavaClasspath

  @TaskAction
  void run() {
    def st2Proj = new SublimeProject(project, 
                                     getDefaultFileExcludePatterns(), 
                                     getDefaultFolderExcludePatterns(), 
                                     getAddDependencyProjects(), 
                                     getGenerateSublimeJavaClasspath())
    File destination = new File(project.projectDir, String.format("%s.sublime-project", getSublimeProjectName()))
    destination.write st2Proj.toString()
  }

}