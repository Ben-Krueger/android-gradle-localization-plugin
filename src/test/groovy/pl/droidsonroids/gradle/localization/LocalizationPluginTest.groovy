package pl.droidsonroids.gradle.localization

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * Created by koral on 21.06.14.
 */
class LocalizationPluginTest extends GroovyTestCase {

    @Test
    void testCsvFileConfig() {
        def config=new ConfigExtension()
        config.csvFile = new File(getClass().getResource('valid.csv').getPath())
        parseTestCSV(config)
    }

    @Test
    void testValidFile() {
        println 'testing valid file'
        long a = System.nanoTime()
        parseTestFile('valid.csv')
        println System.nanoTime() - a
        //1 131 572 583
        //1 197 234 868
    }

    @Test
    void testMissingTranslation() {
        println 'testing invalid file'
        try {
            parseTestFile('missingTranslation.csv')
            fail(InputParseException.class.getSimpleName()+' expected')
        }
        catch (InputParseException ignored) {
            println 'expected exception thrown'
        }
    }

    private void parseTestFile(String fileName) {
        def config = new ConfigExtension()
        config.csvFileURI = getClass().getResource(fileName).toURI()
        parseTestCSV(config)
    }

    private static void parseTestCSV(ConfigExtension config) throws IOException {
        def project = ProjectBuilder.builder().build()
        def resDir = project.file('src/main/res')
        try {
            new Parser(config, resDir).parseCells()
        }
        finally {
            resDir.deleteDir()
        }
    }
}
