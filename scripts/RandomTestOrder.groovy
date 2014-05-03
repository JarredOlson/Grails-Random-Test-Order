scriptEnv = 'test'

includeTargets << grailsScript("_GrailsClean")
includeTargets << grailsScript("_GrailsTest")

TEST_PHASE_AND_TYPE_SEPARATOR = ':'
INTEGRATION_TEST_TYPE = "integration"
UNIT_TEST_TYPE = "unit"
FUNCTIONAL_TEST_TYPE = "functional"

target(main: "Runs Unit and Integration tests in random order") {
    depends(checkVersion, configureProxy, parseArguments, cleanTestReports)

    // The test targeting patterns
    def testTargeters = []

    // The params that target a phase and/or type
    def phaseAndTypeTargeters = []

    argsMap.each {
        if (it.key != 'params') {
            testOptions[it.key] = it.value
        }
    }

    // treat pre 1.2 phase targeting args as '¬´phase¬ª:' for backwards compatibility
    ["unit", "integration", "functional", "other"].each {
        if (argsMap[it]) {
            phaseAndTypeTargeters << "${it}${TEST_PHASE_AND_TYPE_SEPARATOR}"
            argsMap.remove(it) // these are not test "options"
        }
    }

    // process the phaseAndTypeTargeters, populating the targetPhasesAndTypes map from _GrailsTest
    phaseAndTypeTargeters.each {
        def parts = it.split(TEST_PHASE_AND_TYPE_SEPARATOR, 2)
        def targetPhase = parts[0] ?: TEST_PHASE_WILDCARD
        def targetType = parts[1] ?: TEST_TYPE_WILDCARD

        if (!targetPhasesAndTypes.containsKey(targetPhase)) targetPhasesAndTypes[targetPhase] = []
        targetPhasesAndTypes[targetPhase] << targetType
    }

    if (argsMap["xml"]) {
        reportFormats = ["xml"]
        createTestReports = false
    }
    else {
        createTestReports = !argsMap["no-reports"]
    }

    def seed

    if (argsMap["seed"] && argsMap["params"]) {
       try {
          seed = new Long(argsMap["params"][0])
       }
       catch (Exception e) {}
    }


    def random = getRandom(seed)

    //Random Test Order Stuff
    testNames = []

     println "\n"
     if (isTestType(UNIT_TEST_TYPE)) {
         def randomizedTestNames = getTestNamesInRandomOrderForTestType(UNIT_TEST_TYPE, random)
         def command = getTestCommandAboutToExecuteForTestTypeAndNames(UNIT_TEST_TYPE, randomizedTestNames)
         printCommand(command)
         testNames.addAll(randomizedTestNames)
     }

     if (isTestType(INTEGRATION_TEST_TYPE)) {
         def randomizedTestNames = getTestNamesInRandomOrderForTestType(INTEGRATION_TEST_TYPE, random)
         def command = getTestCommandAboutToExecuteForTestTypeAndNames(INTEGRATION_TEST_TYPE, randomizedTestNames)
         printCommand(command)
         testNames.addAll(randomizedTestNames)
     }

     if(isTestType(FUNCTIONAL_TEST_TYPE)) {
         def randomizedTestNames = getTestNamesInRandomOrderForFunctionalTests(random)
         def command = getTestCommandAboutToExecuteForTestTypeAndNames(FUNCTIONAL_TEST_TYPE, randomizedTestNames)
         printCommand(command)
         testNames.addAll(randomizedTestNames)
     }

    allTests()
}

def getRandom(seed) {

   if (!seed) {
      seed = new Date().getTime()
   }
   println "Seed used: ${seed}"
   new Random(seed)
}

def printCommand(command) {
    println "About to execute the following test app script..."
    println command
    println "\n"
}

def getArgsAsString() {
    new String(this.binding.args.bytes)
}

def isTestType(testType) {
    getArgsAsString().contains("-${testType}")
}

def getTestCommandAboutToExecuteForTestTypeAndNames(testType, randomizedTestNames) {
    "grails test-app -${testType} " + randomizedTestNames.join(' ')
}

def getTestNamesInRandomOrderForTestType(testType, random) {
    def files = getAllTestNames(testType)
    getFileListForTestTypeFromFileNames(files, random)
}

def getTestNamesInRandomOrderForFunctionalTests(random) {
    def files = getAllTestNamesForFunctionalTests()
    getFileListForTestTypeFromFileNames(files, random)
}

def getFileListForTestTypeFromFileNames(files, random) {
    files = files.collect {it.substring(0, it.indexOf('.'))}

    Collections.shuffle(files, random)
    files
}

def getAllTestNames(testType) {
    def directory = "${getApplicationRootDirectory()}/test/${testType}"
    getAllFilePathsInDirectory(directory)
}

def getAllFilePathsInDirectory(directory) {
    def fileList = []
    addFilePathsToList(directory, fileList)
    fileList
}

def addFilePathsToList(directory, fileList) {
    File rootDirectory = new File(directory)
    rootDirectory.eachFile() { file ->
        if (file.directory) {
            addFilePathsToList(file.path, fileList)
        }
        if (shouldAddFileToList(file)) {
            fileList << file.name
        }
    }
}

def getAllTestNamesForFunctionalTests() {
    def directory = "${getApplicationRootDirectory()}/test"
    getAllFilePathsInDirectoryForFunctionalTests(directory)
}

def getAllFilePathsInDirectoryForFunctionalTests(directory) {
    def fileList = []
    addFilePathsToListForFunctionalTests(directory, fileList)
    fileList
}

def addFilePathsToListForFunctionalTests(directory, fileList) {
    File rootDirectory = new File(directory)
    rootDirectory.eachFile() { file ->
        if (file.directory && shouldUseDirectoryForFunctionalTests(file.name)) {
            addFilePathsToList(file.path, fileList)
        }
        if (shouldAddFileToList(file)) {
            fileList << file.name
        }
    }
}

def shouldUseDirectoryForFunctionalTests(directoryName) {
    !["unit", "integration"].contains(directoryName.toLowerCase())
}

def shouldAddFileToList(file) {
    def upperFileName = file.name.toUpperCase()
    !upperFileName.startsWith(".") && (upperFileName.endsWith("TESTS.GROOVY") || upperFileName.endsWith("TEST.GROOVY"))
}

def getApplicationRootDirectory() {
    flipBackSlashesToForwardSlashes(System.properties['base.dir'])
}

def flipBackSlashesToForwardSlashes(String str) {
    if (str == null) {return null}
    str.replaceAll('\\\\', '/')
}

setDefaultTarget(main)
