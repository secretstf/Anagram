package Anagram;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //Main method
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);//Needed for user input

        System.out.println("Note: This program requires an internet connection");

        //Getting user input
        System.out.println("What is the word you are looking an anagram for?");
        String word = in.next();

        //Create a list of possible permutations of the word and a list for real anagrams
        ArrayList<String> perms = printPermutn(word, "", new ArrayList<String>());
        ArrayList<String> anagrams = new ArrayList<>();

        //Noting that the internet will be needed for checking for anagrams
        System.out.println("\nConnecting to internet ...");

        //Go through all permutations and check which are valid words. Add those to anagrams
        for (String x : perms){
            if(checkWord(x))
                anagrams.add(x);
        }

        anagrams.remove(word);//Remove the original word form anagram list

        //Print out all the anagrams, if there are any
        if (anagrams.size() != 0) {
            System.out.println("\nAnagrams: ");
            for (String x : anagrams) {
                System.out.println(x);
            }
        }
    }

    private static ArrayList<String> printPermutn(String str, String ans, ArrayList curr)
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
            System.out.println("Internet not connected. ERROR!");
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