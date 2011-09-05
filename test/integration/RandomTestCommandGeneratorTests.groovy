import org.junit.Test
import com.randomtestorder.ApplicationDirectory
import com.randomtestorder.RandomTestCommandGenerator

@Mixin(RandomTestCommandGenerator)
@Mixin(ApplicationDirectory)
class RandomTestCommandGeneratorTests extends GroovyTestCase {

    def fileList

    void setUp() {
        super.setUp()
        fileList = []
    }

    void tearDown() {
        super.tearDown()
        fileList.each {File f ->
            f.directory ? f.deleteDir() : f.delete()
        }
    }

    @Test
    void getRandomCommandForUnitTestsFindsTestFilesUnderUnitTestDirectory() {
        createTestFilesInDirectory(getApplicationUnitTestDirectory())
        def results = getRandomCommandForUnitTests()
        assert results.contains("File1Test")
        assert results.contains("File2Tests")
        assert !results.contains("ShouldNotFind")
    }

    @Test
    void getRandomCommandForIntegrationTestsFindsTestFilesUnderUnitTestDirectory() {
        createTestFilesInDirectory(getApplicationIntegrationTestDirectory())
        def results = getRandomCommandForIntegrationTests()
        assert results.contains("File1Test")
        assert results.contains("File2Tests")
        assert !results.contains("ShouldNotFind")
        assert results.contains(this.getClass().name)
    }

    private def createTestFilesInDirectory(rootDirectory) {
        fileList << new File("$rootDirectory/other")
        fileList << new File("$rootDirectory/other/directory")
        File file1 = new File("$rootDirectory/other/directory/File1Test.groovy")
        file1.mkdirs()
        fileList << file1
        fileList << new File("$rootDirectory/another")
        fileList << new File("$rootDirectory/another/directory")
        File file2 = new File("$rootDirectory/another/directory/File2Tests.groovy")
        file2.mkdirs()
        fileList << file2
        File file3 = new File("$rootDirectory/another/directory/ShouldNotFind.groovy")
        file3.mkdirs()
        fileList << file3
    }
}
