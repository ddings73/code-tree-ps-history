import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N;
    private static List<Integer> stack = new ArrayList<>();
    private static List<ArrayDeque<Integer>> mountain = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());
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
                    // 특정 등산 횟수에 대한 현재 산의 높이는 항상 최소 값이다.
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
					
                // mountain.size()는 `최대 등산 수 + 1` 이므로, 케이블 카 횟수를 따로 추가하지 않아도 됨
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
            if(minItem < height){ // 현재 등산횟수의 최소 값보다 크므로, 더 큰 등산횟수를 확인해야함
                left = mid + 1;
            }else{
                idx = mid;
                right = mid - 1;
            }
        }

        return idx;
    }

}