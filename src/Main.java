import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Timmi on 12-Oct-17.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<mCiphertext> ciphertexts = new ArrayList<>();
        ArrayList<mCiphertext> xorCiphertexts = new ArrayList<>();
        ArrayList<Character> charSet = new ArrayList<>();
        int size=0;
        PrintWriter saved = openSaved("./K/kSaved.txt");
        PrintWriter candidateOut = openSaved("./A/kCandidate.txt");
        int option;
        Scanner sc = new Scanner(System.in);
        startLoad(ciphertexts, xorCiphertexts);
        try {
            loadCharset(charSet);
        } catch (IOException e) {
            System.out.println("Cannot import charset");
            return;
        }
        for (mCiphertext ct:ciphertexts) {
            if(size<ct.getByteRepresentation().size()) {
                size = ct.getByteRepresentation().size();
            }
        }
        int[] candidate=new int[size];
        Arrays.fill(candidate,-1);
        loadCode(candidate);
        while (true) {
            System.out.println("What now? 1-Xor two ciphertexts 2-Show saved xored ciphers 3-Show saved not xored ciphertexts 4-Xor every cyphertext with all of the " +
                    "others 5-Look for spaces and identical signs 6-Show possible messages 7-Input key 8-Save candidate 9-Exit");
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                option = -1;
                System.out.println("Wrong number");
            }
            if (option == 1) {
                option1(ciphertexts, xorCiphertexts, sc, saved);
            } else if (option == 2) {
                option2(xorCiphertexts);
            } else if (option == 3) {
                option3(ciphertexts, sc);
            } else if (option == 4) {
                try {
                    option4(ciphertexts, xorCiphertexts, saved);
                } catch (Exception e) {
                    System.out.println("Wrong number");
                }
            } else if (option == 5) {
                option5(ciphertexts,xorCiphertexts,charSet, candidate);
            } else if(option == 6){
                option6(ciphertexts,sc);
            }else if(option==7){
                option7(ciphertexts,candidate,sc);
            }else if(option==8){
                option8(candidate,sc,candidateOut);
            }else if(option == 9){
                if (saved != null) {
                    saved.close();
                }
                if (candidateOut != null) {
                    candidateOut.close();
                }
                break;
            }
        }

    }

    private static PrintWriter openSaved(String c) {
        try {
            File file= new File(c);
            if (file.exists()){
                return new PrintWriter(new BufferedWriter(new FileWriter(c, true)));
            }else{
                System.out.println("Error, save file missing");
            }

        } catch (Exception e) {
            System.out.println("File cannot be opened, no work will be saved");
        }
        return null;

    }

    private static void option1(ArrayList<mCiphertext> ciphertexts, ArrayList<mCiphertext> xorCiphertexts, Scanner sc, PrintWriter out) {
        int option, option2, option3;
        String c;
        System.out.println("Choose first ciphertext");
        try {
            option = Integer.parseInt(sc.nextLine());
            System.out.println("Choose second ciphertext");
            option2 = Integer.parseInt(sc.nextLine());
            c = ciphertexts.get(option - 1).xor(ciphertexts.get(option2 - 1));
            System.out.println("Result:");
            System.out.println(c);
            System.out.println("Save? 1-Yes 2-No");
            option3 = Integer.parseInt(sc.nextLine());
            if (option3 == 1) {
                if (checkIfSaved(xorCiphertexts, option, option2)) {
                    xorCiphertexts.add(new mCiphertext(c, option, option2));
                    if (out != null) {
                        out.println(Integer.toString(option) + " " + Integer.toString(option2));
                        out.println(c);
                    }
                } else {
                    System.out.println("Already saved!");
                }
            }

        } catch (Exception e) {
            System.out.println("Wrong number");
        }
    }

    private static void option2(ArrayList<mCiphertext> xorCiphertexts) {
        int option = 1;
        for (mCiphertext ciphertext : xorCiphertexts) {
            System.out.println("mCiphertext " + Integer.toString(option) + " From " + Integer.toString(ciphertext.getParent1()) + " and " + Integer.toString(ciphertext.getParent2()) + "\n");
            System.out.println(ciphertext.getCiphertext() + "\n");
            option++;

        }
    }

    private static void option3(ArrayList<mCiphertext> ciphertexts, Scanner sc) {
        int option, option3;
        System.out.println("Which? 1-20 Ciphertext 21-all");
        try {
            option3 = Integer.parseInt(sc.nextLine());
            if (option3 == 21) {
                option = 1;
                for (mCiphertext ciphertext : ciphertexts) {
                    System.out.println("Ciphertext " + Integer.toString(option) + "\n");
                    System.out.println(ciphertext.getCiphertext() + "\n");
                    option++;

                }
            } else {
                System.out.println("Ciphertext " + Integer.toString(option3) + "\n");
                System.out.println(ciphertexts.get(option3 - 1).getCiphertext() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Wrong number");
        }
    }

    private static void option4(ArrayList<mCiphertext> ciphertexts, ArrayList<mCiphertext> xorCiphertexts, PrintWriter out) throws Exception {
        String c;
        for (mCiphertext ct : ciphertexts) {
            for (mCiphertext ct1 : ciphertexts) {
                if (ct != ct1) {
                    c = ct.xor(ct1);
                    if (checkIfSaved(xorCiphertexts, ct.getId(), ct1.getId())) {
                        xorCiphertexts.add(new mCiphertext(c, ct.getId(), ct1.getId()));
                        if (out != null) {
                            out.println(Integer.toString(ct.getId()) + " " + Integer.toString(ct1.getId()));
                            out.flush();
                            out.println(c);
                            out.flush();
                        }
                    }
                }
            }
        }
    }

    private static void option5(ArrayList<mCiphertext> ciphertexts, ArrayList<mCiphertext> xorCiphertexts, ArrayList<Character> charset,int[] candidates) {
        boolean check;
        try {
            for (mCiphertext ct : ciphertexts) {
                ct.clearCandidates();
                ArrayList<mCiphertext> list=getXoredWith(ct.getId(),xorCiphertexts);
                for (int w=0;w<ct.getByteRepresentation().size();w++){
                    for (char c: charset){
                        check=check(list,w,c,charset);
                        if(check){
                            ct.addCandidate(w,c);
                        }
                    }
                }
            }
            decipher(ciphertexts,candidates);
        }catch (Exception e){
            e.getStackTrace();
            System.out.println(Arrays.toString(e.getStackTrace())+"\n\n");
        }
    }

    private static void option6(ArrayList<mCiphertext> ciphertexts, Scanner sc){
        String s;
        System.out.println("1-All 2-Distinct");
        if((sc.nextLine()).equals("1")){
            for (mCiphertext ct: ciphertexts) {
                ct.showCandidatesNoNumbers();
            }
        }else{
            try {
                System.out.println("Which one?");
                s=sc.nextLine();
                ciphertexts.get(Integer.parseInt(s)-1).showCandidatesNoNumbers();
            }catch (Exception e){
                System.out.println("Wrong number");
            }
        }
    }
    private static void option7(ArrayList<mCiphertext> ciphertexts,int[] candidate, Scanner sc){
        int option,option2;
        char c;
        String s;
        try {
            System.out.println("Which ciphertext? 1-20");
            option = Integer.parseInt(sc.nextLine()) - 1;
            ciphertexts.get(option).showCandidates();
            System.out.println("One sign or more? 1-One (2+)-More");
            if(sc.nextLine().equals("1")) {
                System.out.println("Which position?");
                option2 = Integer.parseInt(sc.nextLine()) - 1;
                System.out.println("What sign? ASCII");
                c = sc.nextLine().charAt(0);
                addingChars(ciphertexts, option, option2, candidate, c);
                ciphertexts.get(option).showCandidates();
            }else{
                System.out.println("Which position to start?");
                option2 = Integer.parseInt(sc.nextLine()) - 1;
                System.out.println("Signs ASCII");
                s = sc.nextLine();
                for (int i=0;i<s.length();i++){
                    addingChars(ciphertexts, option, option2+i, candidate, s.charAt(i));
                }
                ciphertexts.get(option).showCandidates();

            }
            System.out.println("1-Add another char 2-See results (3+)-Go to main menu");
            option = Integer.parseInt(sc.nextLine());
            if(option==1){
                option7(ciphertexts,candidate,sc);
            }else if (option==2){
                option6(ciphertexts,sc);
            }
        }catch (Exception e){
            System.out.println("Wrong number");
        }

    }
    private static void addingChars(ArrayList<mCiphertext>ciphertexts, int option, int option2, int[]candidate, char c){
        ciphertexts.get(option).getOriginalMessage().get(option2).clear();
        ciphertexts.get(option).getOriginalMessage().get(option2).add(c);
        candidate[option2]=(char)(ciphertexts.get(option).getByteRepresentation().get(option2)^c);
        for (mCiphertext ct:ciphertexts) {
            if(ct.getId()!=option+1 && option2<ct.getOriginalMessage().size()){
                ct.getOriginalMessage().get(option2).clear();
                ct.getOriginalMessage().get(option2).add((char)(ct.getByteRepresentation().get(option2)^candidate[option2]));
            }
        }
    }
    private static void option8(int[] candidate,Scanner sc, PrintWriter out){
        for (int aCandidate : candidate) {
            if(aCandidate!=-1) {
                System.out.printf(String.valueOf(aCandidate)+"|");
            }else{
                System.out.printf("*|");
            }
        }
        System.out.println("\nSave? 1-Yes (2+)-No");
        try {
            if(sc.nextLine().equals("1")){
                if(out!=null) {
                    out.flush();
                    for (int aCandidate : candidate) {
                        out.printf(Integer.toString( aCandidate+1));
                        out.printf("|");
                    }
                    out.println();
                }else{
                    System.out.println("Error while saving!");
                }
            }
        }catch (Exception e){
            System.out.println("Wrong number");
        }
    }
    private static boolean check(ArrayList<mCiphertext> list,int w, int j,ArrayList<Character> charset){
        int x;
        for (mCiphertext ct1: list) {
            if(w<ct1.getByteRepresentation().size()) {
                x = ct1.getByteRepresentation().get(w) ^ j;
                if (check2((char)x,charset)) {
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean check2(char w,ArrayList<Character> charset){
        for (char c: charset){
            if(w==c){
                return false;
            }
        }
        return true;
    }
    private static void startLoad(ArrayList<mCiphertext> ciphertexts, ArrayList<mCiphertext> xorCiphertexts) {
        FileReader in;
        FileReader in1;
        BufferedReader br;
        BufferedReader br1;
        StringBuilder sb;
        String c;
        int x,y;
        System.out.printf("[");
        for (int i = 0; i < 20; i++) {
            try {
                File file = new File("./K/k" + Integer.toString(i + 1) + ".txt");
                if (file.exists()) {
                    in = new FileReader("./K/k" + Integer.toString(i + 1) + ".txt");
                    br = new BufferedReader(in);
                    sb = new StringBuilder();
                    while ((c = br.readLine()) != null) {
                        sb.append(c);
                    }
                    ciphertexts.add(new mCiphertext(sb.toString(), i + 1));
                    System.out.printf("*");
                    br.close();
                    in.close();
                } else {
                    System.out.printf("-");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("]");
        try {
            File file = new File("./K/kSaved.txt");
            if (file.exists()) {
                in1 = new FileReader("./K/kSaved.txt");
                br1 = new BufferedReader(in1);
                while ((c = br1.readLine()) != null) {
                    x = Integer.parseInt(c.substring(0, c.indexOf(' ')));
                    y = Integer.parseInt(c.substring(c.indexOf(' ') + 1));
                    if ((c = br1.readLine()) != null) {
                        xorCiphertexts.add(new mCiphertext(c, x, y));
                    }

                }
                System.out.println("File kSaved loaded");
            } else {
                System.out.println("File kSaved not loaded");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadCode(int[] candidate){
        FileReader in1;
        BufferedReader br1;
        String c,s="";
        int pos,temp,i=0;
        try {
            File file = new File("./A/kCandidate.txt");
            if (file.exists()) {
                in1 = new FileReader("./A/kCandidate.txt");
                br1 = new BufferedReader(in1);
                while ((c = br1.readLine()) != null) {
                    s=c;
                }
                while((pos=s.indexOf('|'))!=-1){
                    temp=Integer.parseInt(s.substring(0,pos));
                    candidate[i] =temp-1;
                    i++;
                    s=s.substring(pos+1,s.length());
                }


                System.out.println("File kCandidate loaded");
            } else {
                System.out.println("File kCandidate not loaded");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadCharset(ArrayList<Character> charSet) throws IOException {
        String s;
        FileReader in1;
        BufferedReader br1;
        File file = new File("./ASCII/charset.txt");
        if(file.exists()){
            in1 = new FileReader("./ASCII/charset.txt");
            br1 = new BufferedReader(in1);
            while ((s=br1.readLine())!=null){
                for (int i=0;i<s.length();i++){
                    charSet.add(s.charAt(i));
                }
            }
            System.out.println("Charset loaded");
        }else{
            System.out.println("Charset not loaded");
        }
    }
    private static ArrayList<mCiphertext> getXoredWith(int id, ArrayList<mCiphertext> xorCiphertexts){
        ArrayList<mCiphertext> list=new ArrayList<>();
        for (mCiphertext ct: xorCiphertexts) {
            if(ct.getParent1()==id || ct.getParent2()==id){
                list.add(ct);
            }
        }
        return list;
    }
    private static boolean checkIfSaved(ArrayList<mCiphertext> xorCiphertexts, int x, int y) {
        for (mCiphertext c : xorCiphertexts) {
            if (c.getParent1() == x && c.getParent2() == y) {
                return false;
            } else if (c.getParent1() == y && c.getParent2() == x) {
                return false;
            }
        }
        return true;
    }
    private static void decipher(ArrayList<mCiphertext> ciphertexts, int[] candidates){
        for (mCiphertext ct: ciphertexts) {
            for(int i=0;i<candidates.length;i++){
                if(i>=ct.getOriginalMessage().size()){
                    break;
                }
                if(candidates[i]>-1){
                    ct.getOriginalMessage().get(i).clear();
                    ct.getOriginalMessage().get(i).add((char)(ct.getByteRepresentation().get(i)^candidates[i]));
                }
            }
        }
    }
}
