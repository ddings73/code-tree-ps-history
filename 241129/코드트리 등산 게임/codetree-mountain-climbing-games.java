import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N;
    private static List<Integer> stack = new ArrayList<>();
    private static List<ArrayDeque<Integer>> mountain = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        /*
            산 랜덤 선택
            오른쪽 이동하면 100만점
            케이블카 타면 100만점
            더이상 이동 못하면 해당 산 높이만큼 점수 추가

            첫 명령과 마지막 명령을 제외한 모든 명령이 산의 추가라면 약 550000의 산 개수가 됨
            오른쪽 이동이 반드시 바로 옆 산으로 가는 것이 아님
            오른쪽에 있는 산 중에서 내 위치보다 높기만 하면 되는듯
        */

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(stk.nextToken());

            if(command == 100){ // 빅뱅
                N = Integer.parseInt(stk.nextToken());
                for(int i = 0; i < N; i++){
                    int height = Integer.parseInt(stk.nextToken());
                    int idx = mountain.size() == 0 ? 0 : binarySearch(height);
                    stack.add(idx);
                    if(idx == mountain.size()) mountain.add(new ArrayDeque<>());
                    mountain.get(idx).addLast(height);
                }
            }else if(command == 200){ // 우공이산
                int height = Integer.parseInt(stk.nextToken());
                int idx = mountain.size() == 0 ? 0 : binarySearch(height);
                stack.add(idx);
                if(idx == mountain.size()) mountain.add(new ArrayDeque<>());
                mountain.get(idx).addLast(height);
                N++;
            }else if(command == 300){ // 지진
                int mountainSeq = stack.get(N - 1);
                mountain.get(mountainSeq).pollLast();
                if(mountain.get(mountainSeq).isEmpty()){
                    mountain.remove(mountainSeq);
                }

                stack.remove(N - 1);
                N--;
            }else if(command == 400){ // 등산 시뮬레이션
                int m_index = Integer.parseInt(stk.nextToken()) - 1;

                int point = (stack.get(m_index) + mountain.size()) * 1000000;
                point += (mountain.get(mountain.size() - 1).peekFirst());
                sb.append(point).append('\n');
            }
        }

        System.out.println(sb.toString());
    }

    private static int binarySearch(int height){
        int idx = mountain.size();
        int left = 0, right = mountain.size() - 1;
        while(left <= right){
            int mid = (left + right)/2;

            int minItem = mountain.get(mid).peekLast();
            if(minItem < height){ // 현재 스택보다 크므로, 더 큰 스택을 확인해야함
                left = mid + 1;
            }else{
                idx = mid;
                right = mid - 1;
            }
        }

        return idx;
    }

}



/*
// 3 2 5 4 3
index{0,0}
mount[0]{3, 2}
mount[2]{5}

*/