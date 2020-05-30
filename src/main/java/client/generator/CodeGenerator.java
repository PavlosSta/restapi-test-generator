package client.generator;

import interfaces.APISpec;

import java.io.File;

abstract class CodeGenerator {

    protected final APISpec apiSpec;

    protected CodeGenerator(APISpec apiSpec) {
        this.apiSpec = apiSpec;
    }

    protected abstract void validateForTargetLanguage();

    protected abstract void generateClientCode(File dest);

    protected abstract void generateTestClientCode(File dest);

    public void generate(File clientDest, File testDest) {
        validateForTargetLanguage();
        generateClientCode(clientDest);
        generateTestClientCode(testDest);
    }
}
