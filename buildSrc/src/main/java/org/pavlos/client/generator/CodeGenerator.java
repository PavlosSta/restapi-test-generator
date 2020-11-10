package org.pavlos.client.generator;

import org.pavlos.interfaces.APISpec;

import java.io.File;

abstract class CodeGenerator {

    protected final APISpec apiSpec;

    protected CodeGenerator(APISpec apiSpec) {
        this.apiSpec = apiSpec;
    }

    protected abstract void validateForTargetLanguage(APISpec apiSpec);

    protected abstract void generateClientCode(File dest);

    protected abstract void generateTestClientCode(File dest);

    protected abstract void generateTestServerCode(File dest);

    public void generateClient(File clientDest, File testClientDest) {
        validateForTargetLanguage(apiSpec);
        generateClientCode(clientDest);
        generateTestClientCode(testClientDest);
    }

    public void generateServer(File testServerDest) {
        generateTestServerCode(testServerDest);
    }
}
