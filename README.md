# TextForge

TextForge is a Java library designed for **NLP preprocessing** and **text similarity** tasks.  
The project aims to provide a set of efficient and easy-to-use tools for researchers and developers working with natural language processing.

---

## Requirements

- **Java 24** (preview features enabled)
- PowerShell (for compilation steps on Windows)

---

After cloning (or downloading) the repository, the project can be compiled using the following steps. 

## Compilation Instructions (Windows)

All steps are performed from the project’s **root folder** (the folder containing `src`).

### Step 1 – Collect Java source files
```powershell
$files = Get-ChildItem -Recurse -Path src\ie -Filter *.java | ForEach-Object { $_.FullName }
```

### Step 2 – Compile the source files
```powershell
javac --enable-preview --release 24 -d out -encoding UTF-8 $files
```

### Step 3 – Package compiled files into a JAR
```powershell
jar cf forge.jar -C out .
```

## Generating JavaDoc JAR
These steps are also performed in the **root folder**.

### Step 1 – Create documentation
```powershell
javadoc --enable-preview --release 24 -d javadoc -sourcepath src -subpackages ie -encoding UTF-8
```

### Step 2 - Package JavaDoc into a JAR
```powershell
jar cf forge-javadoc.jar -C javadoc .
```

This should result in two .jar files (forge.jar and forge-javadoc.jar) that can be used in an IDE and imported into your project.

## Enabling preview features
It may be necessary to explicitly state to used Java preview features in your IDE. For IntelliJ, this can be done by editing the run configuration.

Run -> Edit Configurations -> Main -> Modify Options -> Add VM Options.
In the new "VM Options" text box include "--enable-preivew".