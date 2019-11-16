package com.tlvlp.iot.server.portal.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

/**
 * Parses file based secrets and makes them available as new environment variables.
 * The main use-case is to parse Docker Swarm Secrets.
 * <p>
 * 0) If the spring profile is set to development mode, then skips secret loading
 * The profile variable is checked at two places:
 * - Automatic in the environment variables as spring.profiles.active=dev
 * - Manual in the application arguments. Since they are not available to be injected
 * at this stage, a static springProfileArg is exposed that can be manually set from the
 * the main method, where the arguments are parsed.
 * <p>
 * 1) Looks for two types of environment variables as input:
 * Secrets Folder:
 * - Env. variable with 'SECRETS_FOLDER' in its name (only the first match is used!)
 * - Contains the absolute location of the folder containing the secret files
 * Secret Files:
 * - Env. variables with 'SECRET_FILE' in their names (all matches are used!)
 * - Each contains the name of a secret file
 * <p>
 * 2) Parses the contents of each existing secret file
 * <p>
 * 3) Saves each secret under a new env. variable with a "_PARSED" postfix
 * eg. MY_DB_PASS_SECRET_FILE's contents will be available under MY_DB_PASS_SECRET_FILE_PARSED
 * <p>
 * NOTE:
 * This {@code EnvironmentPostProcessor} class must be registered under resources/META-INF/spring.factories
 * eg.:
 * org.springframework.boot.env.EnvironmentPostProcessor = PATH.IN.PROJECT.SecretsLoader
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecretsLoader implements EnvironmentPostProcessor {

    public static String springProfileArg = "";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.printf("%n%nDiscovering and loading file-based secrets before the service starts.%n");
//        PropertySource<?> system = environment.getPropertySources().get(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
        Map<String, Object> allEnvVariables = environment.getSystemEnvironment();

        var springProfileEnv = getSpringProfileEnvirontmentVariable(allEnvVariables);
        var devProfileName = "dev";
        if(springProfileEnv.equalsIgnoreCase(devProfileName) || springProfileArg.equalsIgnoreCase(devProfileName)) {
            System.out.println("The active Spring profile is set to development. Secrets parsing is skipped!");
            return;
        }

        System.out.println("Searching for secrets folder environment variable:");
        String secretsFolderName = findFolderVar(allEnvVariables);
        String secretsFolder = getEnvValue(secretsFolderName, allEnvVariables);

        System.out.println("Searching for secret file environment variables:");
        List<String> secretFileEnvVariables = findFileVars(allEnvVariables);
        Map<String, String> secretFileNames = getSecretFileNames(secretFileEnvVariables, allEnvVariables);

        System.out.println("Reading secret files:");
        Map<String, Object> secrets = getSecrets(secretFileNames, secretsFolder);
        environment.getPropertySources()
                .addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new MapPropertySource("secrets", secrets));
        System.out.println("Secret strings are now available at the following environment variables:");
        secrets.keySet().forEach(key -> System.out.printf("    %s%n", key));
    }

    private String getSpringProfileEnvirontmentVariable(Map<String, Object> allEnvVariables) {
        var springProfileEnv = allEnvVariables.get("spring.profiles.active");
        return springProfileEnv == null ? "" : springProfileEnv.toString();
    }

    private String findFolderVar(Map<String, Object> allEnvVariables) {
        List<String> secretsFolderList = allEnvVariables.keySet()
                .stream()
                .filter(key -> key.contains("SECRETS_FOLDER"))
                .limit(1)
                .collect(Collectors.toList());
        if (secretsFolderList.isEmpty()) {
            exit("Error! Secrets folder variable is missing!");
        }
        String secretsFolderName = secretsFolderList.get(0);
        System.out.println(String.format("    Secrets folder variable found: %s", secretsFolderName));
        return secretsFolderName;
    }

    private List<String> findFileVars(Map<String, Object> allEnvVariables) {
        List<String> secretFileEnvVariables = allEnvVariables.keySet()
                .stream()
                .filter(key -> key.contains("SECRET_FILE"))
                .peek(key -> System.out.printf("    Secret file variable found: %s%n", key))
                .collect(Collectors.toList());
        if (secretFileEnvVariables.isEmpty()) {
            exit("Error! No secret file environment variable found!");
        }
        return secretFileEnvVariables;
    }

    private Map<String, String> getSecretFileNames(List<String> secretFileEnvVariables, Map<String, Object> allEnvVariables) {
        Map<String, String> secretFileNames = new HashMap<>();
        for (String secretEnv : secretFileEnvVariables) {
            secretFileNames.put(secretEnv, getEnvValue(secretEnv, allEnvVariables));
        }
        return secretFileNames;
    }

    private String getEnvValue(String variableName, Map<String, Object> allEnvVariables) {
        return String.valueOf(allEnvVariables.get(variableName));
    }


    private Map<String, Object> getSecrets(Map<String, String> secretFileNames, String secretsFolder) {
        return secretFileNames.keySet().stream()
                .collect(Collectors.toMap(
                        secretEnv -> getNewName(secretEnv),
                        secretEnv -> getSecretFromFile(secretFileNames.get(secretEnv), secretsFolder)));
    }

    private String getNewName(String oldName) {
        return String.format("%s_PARSED", oldName);
    }


    private String getSecretFromFile(String secretName, String secretsFolder) {
        String secretPath = String.format("%s/%s", secretsFolder, secretName);
        String secret = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(secretPath))) {
            secret = reader.readLine();
            System.out.printf("    Successfully read: %s%n", secretName);
        } catch (IOException e) {
            exit(String.format("Error loading secret: %s\n%s", secretName, e));
        }
        return secret;
    }

    private void exit(String msg) {
        System.err.println(msg);
        System.err.println("Exiting application.");
        System.exit(1);
    }

}
