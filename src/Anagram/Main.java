package Anagram;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("What is the word you are looking an anagram for?");
        String word = in.next();

        ArrayList<String> perms = printPermutn(word, "", new ArrayList<String>());
        ArrayList<String> anagrams = new ArrayList<>();
        for (String x : perms){
            if(checkWord(x))
                anagrams.add(x);
        }

        for(String x : anagrams){
            System.out.println(x);
        }
    }

    static ArrayList<String> printPermutn(String str, String ans, ArrayList curr)
    {
        if (str.length() == 0){
            curr.add(ans);
            return curr;
        }
        else{
            for (int i = 0; i < str.length(); i++) {
                char currentChar = str.charAt(i);

                String newStr = str.substring(0, i) + str.substring(i+1);
                String newAns = ans + currentChar;

                curr = printPermutn(newStr, newAns, curr);
            }
        }
        return curr;
    }

    private static boolean checkWord(String word){
        String curr = "";
        try {
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/"+word);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            curr = convertResultToString(inputStream);
        } catch (MalformedURLException e) {
            return false;
        } catch(ProtocolException e){
            return false;
        } catch (FileNotFoundException e){
            return false;
        } catch (IOException e){
            return false;
        }
        return true;
    }

    private static String convertResultToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String curr = "";

        while(true){
            try {
                if(!((curr = bufferedReader.readLine()) != null)){
                    stringBuilder.append('\n');
                }
                stringBuilder.append(curr);
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }
}