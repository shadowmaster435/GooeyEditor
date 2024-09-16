To add this to your project

create a folder in your project root called "libs"

download the mod and put it in this folder

put this in your build.gradle repositories
```
    flatDir {
            dirs "./libs"
    }
```

And this in your dependancies
```
    modImplementation 'org.shadowmaster435:GooeyEditor-0.0.1'
```
