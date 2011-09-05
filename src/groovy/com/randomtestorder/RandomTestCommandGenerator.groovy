package com.randomtestorder

@Mixin(ApplicationDirectory)
class RandomTestCommandGenerator {


    def getRandomCommandForIntegrationTests() {
        def testType = "integration"
        def files = getAllTestNames(testType)
        getCommandFor(testType, files)

    }

    def getCommandFor(testType, files) {
        files = files.collect {it.substring(0, it.indexOf('.'))}
        Collections.shuffle(files)
        "grails test-app -${testType} " + files.join(' ')
    }

    def getRandomCommandForUnitTests() {
        def testType = "unit"
        def files = getAllTestNames(testType)
        getCommandFor(testType, files)
    }

    def getAllTestNames(testType) {
        def testFolder = "${getApplicationRootDirectory()}/test/${testType}"
        localGetAllFilePathsInDirectory(testFolder)
    }

    def localGetAllFilePathsInDirectory(directory) {
        def fileList = []
        localAddFilePathsToList(directory, fileList)
        fileList
    }

    def localAddFilePathsToList(directory, fileList) {
        File rootDirectory = new File(directory)
        rootDirectory.eachFile() { file ->
            if (file.directory) {
                localAddFilePathsToList(file.path, fileList)
            }
            if (shouldAddFileToList(file)) {
                fileList << file.name
            }
        }
    }

    def shouldAddFileToList(file) {
        def upperFileName = file.name.toUpperCase()
        //TODO: Switch to regex
        //TODO: Should add java test files
        !upperFileName.startsWith(".") && (upperFileName.endsWith("TESTS.GROOVY") || upperFileName.endsWith("TEST.GROOVY"))
    }

}
