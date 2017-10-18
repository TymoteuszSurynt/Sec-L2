import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Timmi on 12-Oct-17.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList <Ciphertext> ciphertexts=new ArrayList<Ciphertext>();
        ArrayList <Ciphertext> xorCiphertexts=new ArrayList<Ciphertext>();
        PrintWriter out=openSaved();
        int option,option2,option3;
        String c;
        Scanner sc = new Scanner(System.in);
        startLoad(ciphertexts,xorCiphertexts);

        while(true){
            System.out.println("What now? 1-Xor two ciphertexts 2-Show saved xored ciphers 3-Show saved not xored ciphertexts 4-Xor every cyphertext with all of the others 9-Exit");
            try{
                option=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                option=-1;
                System.out.println("Wrong number");
            }
            if (option==1){
                System.out.println("Choose first ciphertext");
                try{
                    option=Integer.parseInt(sc.nextLine());
                    System.out.println("Choose second ciphertext");
                    option2=Integer.parseInt(sc.nextLine());
                    c=ciphertexts.get(option-1).xor(ciphertexts.get(option2-1));
                    System.out.println("Result:");
                    System.out.println(c);
                    System.out.println("Save? 1-Yes 2-No");
                    option3=Integer.parseInt(sc.nextLine());
                    if(option3==1){
                        if(checkIfSaved(xorCiphertexts,option,option2)) {
                            xorCiphertexts.add(new Ciphertext(c, option, option2));
                            if (out != null) {
                                out.println(Integer.toString(option) + " " + Integer.toString(option2));
                                out.println(c);
                            }
                        }else {
                            System.out.println("Already saved!");
                        }
                    }

                }catch (Exception e){
                    System.out.println("Wrong number");
                }
            }else if(option==2){
                option=1;
                for (Ciphertext ciphertext:xorCiphertexts) {
                    System.out.println("Ciphertext "+Integer.toString(option)+" From "+Integer.toString(ciphertext.getParent1())+" and "+Integer.toString(ciphertext.getParent2())+"\n");
                    System.out.println(ciphertext.getCiphertext()+"\n");
                    option++;

                }
            }else if(option==3){
                System.out.println("Which? 1-20 ciphertext 21-all");
                try {
                    option3 = Integer.parseInt(sc.nextLine());
                    if (option3 == 21) {
                        option = 1;
                        for (Ciphertext ciphertext : ciphertexts) {
                            System.out.println("Ciphertext " + Integer.toString(option) + "\n");
                            System.out.println(ciphertext.getCiphertext() + "\n");
                            option++;

                        }
                    }else{
                        System.out.println("Ciphertext " + Integer.toString(option3) + "\n");
                        System.out.println(ciphertexts.get(option3-1).getCiphertext() + "\n");
                    }
                }catch (Exception e){
                    System.out.println("Wrong number");
                }
            }else if(option==4){
                try{
                    for (Ciphertext ct: ciphertexts){
                        for (Ciphertext ct1: ciphertexts) {
                            if(ct!=ct1){
                                c=ct.xor(ct1);
                                if(checkIfSaved(xorCiphertexts,ct.getId(),ct1.getId())) {
                                    xorCiphertexts.add(new Ciphertext(c, ct.getId(),ct1.getId()));
                                    if (out != null) {
                                        out.println(Integer.toString(ct.getId()) + " " + Integer.toString(ct1.getId()));
                                        out.println(c);
                                    }
                                }
                            }
                        }
                    }


                }catch (Exception e){
                    System.out.println("Wrong number");
                }
            }else if(option==2){
                option=1;
                for (Ciphertext ciphertext:xorCiphertexts) {
                    System.out.println("Ciphertext "+Integer.toString(option)+" From "+Integer.toString(ciphertext.getParent1())+" and "+Integer.toString(ciphertext.getParent2())+"\n");
                    System.out.println(ciphertext.getCiphertext()+"\n");
                    option++;

                }

            }else if(option==9){
                if(out!=null) {
                    out.close();
                }
                break;
            }
        }

    }
    private static PrintWriter openSaved(){
        try{
            return new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\kSaved.txt",true)));
        }catch (Exception e){
            System.out.println("File cannot be opened, no work will be saved");
        }
        return null;

    }
    private static void startLoad(ArrayList<Ciphertext> ciphertexts, ArrayList<Ciphertext> xorCiphertexts){

        FileReader in;
        FileReader in1;
        BufferedReader br;
        BufferedReader br1;
        StringBuilder sb;
        String c;
        int x,y;
        for (int i=0;i<20;i++){
            try {
                File file = new File ("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\k" + Integer.toString(i + 1) + ".txt");
                if (file.exists()) {
                    in = new FileReader("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\k" + Integer.toString(i + 1) + ".txt");
                    br = new BufferedReader(in);
                    sb = new StringBuilder();
                    while ((c = br.readLine()) != null) {
                        sb.append(c);
                    }
                    ciphertexts.add(new Ciphertext(sb.toString(),i+1));
                    System.out.println("File" + Integer.toString(i + 1) + " loaded");
                    br.close();
                    in.close();
                }else{
                    System.out.println("File" + Integer.toString(i + 1) + " not loaded");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            File file = new File ("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\kSaved.txt");
            if (file.exists()) {
                in1 = new FileReader("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\kSaved.txt");
                br1 = new BufferedReader(in1);
                while ((c = br1.readLine()) != null) {
                    x = Integer.parseInt(c.substring(0, c.indexOf(' ')));
                    y = Integer.parseInt(c.substring(c.indexOf(' ') + 1));
                    sb = new StringBuilder();
                    if ((c = br1.readLine())!= null) {
                        xorCiphertexts.add(new Ciphertext(c,x,y));
                    }

                }
                System.out.println("File kSaved loaded");
            }else{
                System.out.println("File kSaved not loaded");
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static boolean checkIfSaved(ArrayList<Ciphertext> xorCiphertexts, int x, int y){
        for (Ciphertext c: xorCiphertexts) {
            if(c.getParent1()==x && c.getParent2()==y){
                return false;
            }else if(c.getParent1()==y && c.getParent2()==x){
                return false;
            }
        }
        return true;
    }
}
