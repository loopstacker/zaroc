package be.kdg.integration.gameapplication.model.authservices.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Hashes and verifies passwords using the PBKDF2WithHmacSHA256 algorithm.
 * <p>
 * Passwords are never stored in plain text, each password is combined
 * with a unique salt before hashing to prevent rainbow table attacks.
 * </p>
 */

public class PasswordService implements PasswordEncoder{

    /**
     * Generates a cryptographically secure random salt. Used by {@link PasswordRecoveryService} and {@link RegistrationService}
     *
     * @return a 16-byte random salt generated via {@link SecureRandom}
     */

    public byte[] generateSalt(){
        byte[] salt = new byte[16]; //that is 16 bytes, which is exactly 24 characters. Always.

        new SecureRandom() //it uses systems entropy to generate 16 bytes salt
                .nextBytes(salt); //fils the salt array with bytes
        return salt;
    }

    /**
     * Encodes a raw salt byte array into a Base64 string. Used by {@link PasswordRecoveryService} and {@link RegistrationService}
     *
     * @param salt the raw salt bytes to encode
     * @return a Base64-encoded string representation of the salt
     */

    public String saltToString(byte[] salt){ //that one is for database so now we can store it as string
        String hashBase64 = Base64.getEncoder().encodeToString(salt);
        return hashBase64;
    }


    /**
     * Generates a salt as a string
     * @return salt in a string format
     */
    public String generateSaltAsString(){
        return saltToString(generateSalt());
    }


    private byte[] saltToBytes(String salt){    //that one is for java, since PBKDF works with byte array
        return Base64.getDecoder().decode(salt);
    }

    /**
     * Hashes a password by combining it with the given salt using PBKDF2WithHmacSHA256. Used by {@link LoginService}, {@link RegistrationService} and {@link PasswordRecoveryService}
     *
     * @param password the raw password to hash
     * @param salt     the generated randomly salt to combine with the password
     * @return an encoded, salted and hashed string password
     * @throws PasswordServiceException if the hashing algorithm is unavailable or the key specification is invalid. If it is thrown, check internal configurations of encoders
     */

    public String addSaltToPasswordAndEncodeIt(String password, String salt) throws PasswordServiceException{
        SecretKeyFactory factory;
        try{
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //that one throws and exception
            //That row means that it will use PBKDF2 algorithm which uses SHA256 hash function.
            //SHA256 is not secure by itself but PBKDF2 makes
            //it secure, shuffling password with salt thousands times
        }catch (NoSuchAlgorithmException e) {
            throw new PasswordServiceException();
        }

        PBEKeySpec spec = new PBEKeySpec( //that is an object, which will set the preferences on how should be the hashed password generated
                password.toCharArray(), //array password, where 1 cell contains from 2 bytes to 4 bytes
                saltToBytes(salt),  //byte[] password
                600000, //iterations
                256 //length in bites. Which is 32 bytes and which is exact 44 characters.
        );
        byte[] hash;
        try{
            hash = factory.generateSecret(spec).getEncoded(); //generates hashed password using preferences (spec) that we set
        }catch (InvalidKeySpecException e) {
            throw new PasswordServiceException();
        }
        return Base64.getEncoder().encodeToString(hash); //returns the string value of encoded hash
    }

    /**
     * Verifies whether a raw password matches a hashed password from the database. Used by {@link LoginService}
     *
     * @param hashedSaltedPasswordFromDB the stored hashed password to compare against
     * @param saltFromDB the salt created used during registration
     * @param passwordToCheck the raw password to verify
     * @return {@code true} if the password matches, {@code false} otherwise
     * @throws PasswordServiceException if hashing fails during verification
     */

    public boolean confirmPassword(String hashedSaltedPasswordFromDB, String saltFromDB, String passwordToCheck) throws PasswordServiceException{
        try{
            String saltedPasswordToCheck = addSaltToPasswordAndEncodeIt(passwordToCheck, saltFromDB); //we mix passed password with salt that we got from database
            return hashedSaltedPasswordFromDB.equals(saltedPasswordToCheck);//we check is the mixed salt we just made is equals to the salt, that was made during the registration
        }catch (PasswordServiceException e){
            throw e;
        }
    }
}
