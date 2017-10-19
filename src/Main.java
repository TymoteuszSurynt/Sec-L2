import java.io.*;
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
        PrintWriter saved = openSaved("./K/kSaved.txt");
        PrintWriter candidate = openSaved("./K/kCandidate.txt");
        int option;
        Scanner sc = new Scanner(System.in);
        startLoad(ciphertexts, xorCiphertexts);

        while (true) {
            System.out.println("What now? 1-Xor two ciphertexts 2-Show saved xored ciphers 3-Show saved not xored ciphertexts 4-Xor every cyphertext with all of the others 5-Look for spaces and identical signs 6-Show possible messages 9-Exit");
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
                option5(ciphertexts,xorCiphertexts);
            } else if(option == 6){
                option6(ciphertexts,sc);
            }else if (option == 9) {
                if (saved != null) {
                    saved.close();
                }
                break;
            }
        }

    }

    private static PrintWriter openSaved(String c) {
        try {
            return new PrintWriter(new BufferedWriter(new FileWriter(c, true)));
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
        System.out.println("Which? 1-20 ciphertext 21-all");
        try {
            option3 = Integer.parseInt(sc.nextLine());
            if (option3 == 21) {
                option = 1;
                for (mCiphertext ciphertext : ciphertexts) {
                    System.out.println("mCiphertext " + Integer.toString(option) + "\n");
                    System.out.println(ciphertext.getCiphertext() + "\n");
                    option++;

                }
            } else {
                System.out.println("mCiphertext " + Integer.toString(option3) + "\n");
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
                        System.out.println(Integer.toString(ct.getId()) + " " + Integer.toString(ct1.getId()));
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

    private static void option5(ArrayList<mCiphertext> ciphertexts, ArrayList<mCiphertext> xorCiphertexts) {
        boolean check;
        try {
            for (mCiphertext ct : ciphertexts) {
                ArrayList<mCiphertext> list=getXoredWith(ct.getId(),xorCiphertexts);
                //System.out.println();
                //System.out.println(ct.getId());
                for (int w=0;w<ct.getByteRepresentation().size();w++){
                    //System.out.println(Integer.toString(w)+"/"+Integer.toString(ct.getByteRepresentation().size()));
                    //System.out.flush();
                    //System.out.printf(Integer.toString(w)+" ");
                    for (int j = 65; j < 91; j++){
                        check=check(list,w,j);
                        if(check){
                            //System.out.println(Integer.toString(w)+" "+Integer.toString(j));
                            ct.addCandidate(w,j);
                        }

                    }
                    for (int j = 97; j < 123; j++) {
                        check=check(list,w,j);
                        if(check){
                            ct.addCandidate(w,j);
                        }

                    }
                    check=check(list,w,' ');
                    if(check){
                        ct.addCandidate(w,' ');
                    }
                    check=check(list,w,'.');
                    if(check){
                        ct.addCandidate(w,'.');
                    }
                    check=check(list,w,'!');
                    if(check){
                        ct.addCandidate(w,'!');
                    }
                    check=check(list,w,'?');
                    if(check){
                        ct.addCandidate(w,'?');
                    }
                    check=check(list,w,',');
                    if(check){
                        ct.addCandidate(w,',');
                    }
                    check=check(list,w,':');
                    if(check){
                        ct.addCandidate(w,':');
                    }
                    check=check(list,w,';');
                    if(check){
                        ct.addCandidate(w,';');
                    }
                    check=check(list,w,'"');
                    if(check){
                        ct.addCandidate(w,'"');
                    }
                }
            }
        }catch (Exception e){
            e.getStackTrace();
            System.out.println(Arrays.toString(e.getStackTrace())+"\n\n");
        }
    }

    private static void option6(ArrayList<mCiphertext> ciphertexts, Scanner sc){
        String s;
        System.out.println("1-All 2-Distinct");
        if((s=sc.nextLine()).equals("1")){
            for (mCiphertext ct: ciphertexts) {
                ct.showCandidates();
            }
        }else{
            try {
                System.out.println("Which one?");
                s=sc.nextLine();
                ciphertexts.get(Integer.parseInt(s)).showCandidates();
            }catch (Exception e){
                System.out.println("Wrong number");
            }
        }
    }
    private static boolean check(ArrayList<mCiphertext> list,int w, int j){
        int x;
        for (mCiphertext ct1: list) {
            if(w<ct1.getByteRepresentation().size()) {
                x = ct1.getByteRepresentation().get(w) ^ j;
                if ((x < 65 && x!='?' && x!=' ' && x!=',' && x!='.' && x!='!' && x!=':' && x!=';' && x!='"' )|| (x > 90 && x < 97)||(x>122)) {
                    return false;
                }
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
                    System.out.println("File" + Integer.toString(i + 1) + " loaded");
                    br.close();
                    in.close();
                } else {
                    System.out.println("File" + Integer.toString(i + 1) + " not loaded");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
}
