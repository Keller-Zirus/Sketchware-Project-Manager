# Project Manager
Sketchware Projects Manager

The best library for working with Sketchware projects is getting, deleting, modifying projects and much more.

# Install

Add repository
```gradle
repositories {
	maven { url 'https://jitpack.io' }
}
```
And add dependency
```gradle		
dependencies {
	implementation 'com.github.justneon33:Sketchware-Project-Manager:+'
}
```

# How to use

## Kotlin

```kotlin
val projectManager = ProjectsManager(defaultPaths)
// get projects list
val projectsList = projectManager.projectsList
// delete project at 0 position
projectManager.projectsList[0].delete()
// copy project 
projectManager.projectsList[0].copy(ProjectPaths(..))
```

## Java

```java
ProjectsManager projectsManager = new ProjectsManager(getDefaultPaths());
// get list of projects
ArrayList<ProjectModel> projects = projectsManager.getProjectsList();
// delete project
delete(projects.get(0));
// copy project
copy(new ProjectPaths(..));
```

### You can read / write project data file

## Kotlin

``` Kotlin
// read project data
val projectListFile = projectManager.decryptAndRead("path_to_project_file")
// write project data
ProjectListFileReader.write("path_to_project_file", projectListFile)
```

## Java
``` Java
// read project file
ListFileModel listFileModel = projectsManager.decryptAndRead("path_to_project_file");
// write project file
ProjectListFileReader.INSTANCE.write("path_to_project_file", listFileModel);
```
