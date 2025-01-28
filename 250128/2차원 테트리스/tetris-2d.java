import java.util.*;
import java.io.*;

public class Main {

    private static int K;
    
    private static class Block{
        int type;
        int r, c;
        Block(int type, int r, int c){
            this.type = type;
            this.r = r;
            this.c = c;
        }

        Block clone(boolean reverse){
            if(!reverse) return new Block(this.type, this.r, this.c);
            else{
                int type = this.type == 2 ? 3 
                    : this.type == 3 
                        ? 2 : 1;
                
                return new Block(type, this.c, this.r);
            }  
        }
    }

    public static void main(String[] args) {
        List<Block> block_input = input();

        int[][] yellow = new int[10][4];
        int[][] red = new int[10][4];

        /*
            red는 type2와 type3 반전
        */ 

        int score = 0;
        for(Block b : block_input){
            score += putInto(yellow, b.clone(false));
            score += putInto(red, b.clone(true));
        }
        System.out.println(score);

        int count = 0;
        for(int i = 6; i < 10; i++){
            for(int j = 0; j < 4; j++){
                if(yellow[i][j] == 1) count++;
                if(red[i][j] == 1) count++;
            }
        }
        System.out.println(count);
    
    }

    private static int putInto(int[][] arr, Block b){
        int score = 0;

        // 블럭 두기
        while(b.r < 10){
            if(!nextAreaCheck(arr, b)) break;
            b.r++;
        }

        arr[b.r][b.c] = 1;
        if(b.type == 2) arr[b.r][b.c + 1] = 1;
        else if(b.type == 3)arr[b.r + 1][b.c] = 1;


        // 행 체크 및 포인트 갱신
        for(int i = 9; i > 5; i--){
            boolean full = true;
            for(int j = 0; full && j < 4; j++){
                if(arr[i][j] == 0) full = false;
            }

            if(!full) continue;
            score++;
            for(int j = i; j > 3; j--){
                for(int k = 0; k < 4; k++){
                    arr[j][k] = arr[j - 1][k];
                }
            }
            i++;
        }

        // 연한 부분 체크
        for(int i = 4; i <= 5; i++){
            boolean empty = true;
            for(int j = 0; empty && j < 4; j++){
                if(arr[i][j] == 1) empty = false;
            }

            if(empty) continue;
            for(int j = 9; j > 3; j--){
                for(int k = 0; k < 4; k++){
                    arr[j][k] = arr[j - 1][k];
                }
            }
            i--;
        }

        return score;
    }

    private static boolean nextAreaCheck(int[][] arr, Block b){
        int r = b.r + 1;
        int c = b.c;
        
        if(r >= 10) return false;

        if(b.type == 2){
            return arr[r][c] == 0 && c + 1 < 4 && arr[r][c + 1] == 0;
        }else if(b.type == 3){
            return arr[r][c] == 0 && r + 1 < 10 && arr[r + 1][c] == 0; 
        }else 
            return arr[r][c] == 0;
    }

    private static List<Block> input(){
        List<Block> block_input = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            K = Integer.parseInt(br.readLine());

            for(int i = 0; i < K; i++){
                StringTokenizer stk  =new StringTokenizer(br.readLine());
                int t = Integer.parseInt(stk.nextToken());
                int r = Integer.parseInt(stk.nextToken());
                int c = Integer.parseInt(stk.nextToken());

                block_input.add(new Block(t, r, c));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return block_input;
    }
}