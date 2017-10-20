import java.util.ArrayList;

/**
 * Created by Timmi on 9-Oct-17.
 */
class mCiphertext {
    private String ciphertext;
    private int parent1=0, parent2=0;
    private int id=0;
    private ArrayList<Character> byteRepresentation;
    private ArrayList<ArrayList<Character>> originalMessage;

    mCiphertext(String ciphertext, int id) {
        this.ciphertext = ciphertext;
        this.id = id;
        String s;
        byteRepresentation=new ArrayList<>();
        originalMessage=new ArrayList<>();
        for(int i=0; i<ciphertext.length();i+=9){
            s = ciphertext.substring(i, i + 8);
            byteRepresentation.add((char)Integer.parseInt(s,2));
            originalMessage.add(new ArrayList<>());
        }
    }

    mCiphertext(String ciphertext, int parent1, int parent2) {
        this.ciphertext = ciphertext;
        this.parent1 = parent1;
        this.parent2 = parent2;
        String s;
        byteRepresentation=new ArrayList<>();
        originalMessage=new ArrayList<>();
        for(int i=0; i<ciphertext.length();i+=9){
            s = ciphertext.substring(i, i + 8);
            byteRepresentation.add((char)Integer.parseInt(s,2));
            originalMessage.add(new ArrayList<>());
        }
    }

    String xor(mCiphertext ciphertext) {
        StringBuilder stringBuilder = new StringBuilder();
        int size;
        if (ciphertext.getCiphertext().length() < this.ciphertext.length()) {
            size = ciphertext.getCiphertext().length();
        } else {
            size = this.ciphertext.length();
        }
        for (int i = 0; i < size; i++) {
            if (this.ciphertext.charAt(i) == ' ') {
                stringBuilder.append(' ');
            } else {
                switch (this.ciphertext.charAt(i)) {
                    case '1':
                        switch (ciphertext.getCiphertext().charAt(i)) {
                            case '1':
                                stringBuilder.append('0');
                                break;
                            case '0':
                                stringBuilder.append('1');
                                break;
                        }
                        break;
                    case '0':
                        switch (ciphertext.getCiphertext().charAt(i)) {
                            case '1':
                                stringBuilder.append('1');
                                break;
                            case '0':
                                stringBuilder.append('0');
                                break;
                        }
                        break;
                }
            }
        }
        return stringBuilder.toString();
    }

    String getCiphertext() {
        return ciphertext;
    }

    int getParent1() {
        return parent1;
    }

    int getParent2() {
        return parent2;
    }

    int getId() {
        return id;
    }

    ArrayList<Character> getByteRepresentation() {
        return byteRepresentation;
    }

    ArrayList<ArrayList<Character>> getOriginalMessage() {
        return originalMessage;
    }
    void addCandidate(int w, int j){
        originalMessage.get(w).add((char)j);
    }

    void showCandidates(){
        int i=0;
        for (ArrayList<Character> list: originalMessage) {
            i++;
            System.out.printf("(["+Integer.toString(i)+"]");
            for (char b: list) {
                System.out.printf(b+"");
            }
            System.out.printf(")");
        }
        System.out.printf("\n");
    }
    void showCandidatesNoNumbers(){
        for (ArrayList<Character> list: originalMessage) {
            System.out.printf("(");
            for (char b: list) {
                System.out.printf(""+b);
            }
            System.out.printf(")");
        }
        System.out.println("");
    }
    void clearCandidates(){
        for (ArrayList<Character> c:originalMessage) {
            c.clear();
        }
    }
}
