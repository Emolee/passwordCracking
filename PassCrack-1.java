import java.security.*;
import java.util.*;
import java.io.*;
//i helped nikki nicolas and suri sahay with this assignment.
public class PassCrack {

    static String getHash(String salt, String pwd) throws NoSuchAlgorithmException {
        //hash's work by:
        //think about it as a deterministic way to create a "long mess of characters."
        //*note deterministic means: give in input, gives same output.
        //if you change one character, it will alter the entire string.
        //databases don't save your password, but the hash that's calculated by your password.
        //thus, your password isn't effectively saved into the database.
        //*look up hash collision out of curiosity.
        //*hash look-up/hash table look-up: hash a password that is confirmed to work
        //for database.
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(md.digest((salt + pwd).getBytes()));
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        //we want to:
        //1. read through file
        //2. split
        Scanner scanner = new Scanner(System.in);
        InputStream inputStream = PassCrack.class.getResourceAsStream("/passwords.txt");
        String data = new String(inputStream.readAllBytes());
        String users[] = data.split("\r\n"); //this is all vault code LOL
        ArrayList<PassContainter> passContainerList = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            String userSplit[] = users[i].split(":");
            PassContainter userContainer = new PassContainter();
            userContainer.accountName = userSplit[0];
            userContainer.salt = userSplit[1];
            userContainer.pwd = userSplit[2];
            passContainerList.add(userContainer);
        }

        for (PassContainter pass : passContainerList) {
            getPassword(pass);
        }
    }

    private static void getPassword(PassContainter passContainter) throws NoSuchAlgorithmException, IOException {
        String dictSalt = passContainter.salt;
        File dictFile = new File("src/realhuman_phill.txt");
        BufferedReader br = new BufferedReader(new FileReader(dictFile));
        String dictPass;
        while ((dictPass = br.readLine()) != null) {
            if (passContainter.pwd.equals(getHash(dictSalt, dictPass))) {
                System.out.println("Account name " + passContainter.accountName + "'s password is: " + dictPass);
                return;
            }
        }
        System.out.println("Account name " + passContainter.accountName + " could not be found.");
    }
    //this method above gets the dictionary file we downloaded and goes through each input with a buffered reader.
    //we check to see if the dictPassword is equal to the br input.
    //if it is, then we return the account name and it's password.
    //if not, then we say it's not found.

    //container class for essential variables:
    static class PassContainter {
        String pwd;
        String salt;
        String accountName;
    }
}
