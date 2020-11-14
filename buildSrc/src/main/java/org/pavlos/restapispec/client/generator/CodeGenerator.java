package org.pavlos.restapispec.client.generator;

import freemarker.template.Configuration;
import org.pavlos.restapispec.interfaces.APISpec;

import java.io.File;

abstract class CodeGenerator {

    protected final APISpec apiSpec;

    protected final Configuration cfg;

    protected CodeGenerator(APISpec apiSpec, Configuration cfg) {
        this.apiSpec = apiSpec;
        this.cfg = cfg;
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