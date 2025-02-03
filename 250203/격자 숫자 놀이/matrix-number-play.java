import java.util.*;
import java.io.*;

public class Main {

    private static int R, C, K;
    public static void main(String[] args) {
        int row = 3, col = 3;

        List<List<Integer>> list = new ArrayList<>();
        List<Map<Integer, Integer>> rowMapList = new ArrayList<>();
        input(row, col, list, rowMapList);

        /*
            각 행과 열마다 빈도 수를 관리해야 하고,
            빈도 수에 따라 정렬할 수 있어야 하며,
            컬렉션이 유동적으로 증가할 수 있어야 함

            => 배열보다는 리스트가 적합
            => 행, 열마다 Map으로 빈도 수를 관리해야 함
        */
        int sec = 0;
        while(sec++ <= 100){
            if(row >= col){
                int newCol = col;
                for(int i = 0; i < row; i++){
                    sortTarget(list.get(i), rowMapList.get(i));
                    List<Integer> newRow = new ArrayList<>();
                    Map<Integer, Integer> newMap = new HashMap<>();
                    
                    // 각 행에서 주어진 조건으로 정렬된 list.get(i)를 이용해 새로운 행 생성
                    Set<Integer> keySet = new HashSet<>();
                    for(Integer key : list.get(i)){
                        if(key == 0 || keySet.contains(key)) continue;
                        keySet.add(key);

                        newRow.add(key);
                        newMap.merge(key, 1, Integer::sum);

                        if(newRow.size() == 100) break;

                        Integer value = rowMapList.get(i).get(key);
                        newRow.add(value);
                        newMap.merge(value, 1, Integer::sum);
                        
                        if(newRow.size() == 100) break;
                    }

                    newCol = Math.max(newCol, newRow.size());
                    
                    // 새로운 행을 현재 행과 변경
                    list.set(i, newRow);
                    rowMapList.set(i, newMap);
                }

                // 새로운 col 길이를 이용하여 빈 공간 0으로 채우기
                for(int i = 0; i < row; i++){
                    List<Integer> target = list.get(i);
                    if(target.size() == newCol) continue;

                    int gap = newCol - target.size();
                    while(gap-- > 0){
                        target.add(0);
                    }
                }

                col = newCol;
            }else{
                int newRow = row;

                // 전채 행을 순회하며 각 열에 대한 숫자정보 수집
                List<List<Integer>> newColList = new ArrayList<>();
                for(int j = 0; j < col; j++){
                    List<Integer> colList = new ArrayList<>();
                    Map<Integer, Integer> colMap = new HashMap<>();
                    for(int i = 0; i < row; i++){
                        Integer num = list.get(i).get(j);

                        if(num == 0) continue;
                        colList.add(num);
                        colMap.merge(num, 1, Integer::sum);
                    }

                    // 수집된 열 정보를 조건에 맞게 정렬
                    sortTarget(colList, colMap);
                    List<Integer> resultList = new ArrayList<>();
                    
                    // 정렬된 숫자를 이용하여 새로운 열 생성
                    Set<Integer> keySet = new HashSet<>();
                    for(Integer key : colList){
                        if(keySet.contains(key)) continue;
                        keySet.add(key);

                        resultList.add(key);
                        if(resultList.size() == 100) break;

                        Integer value = colMap.get(key);
                        resultList.add(value);
                        if(resultList.size() == 100) break;
                    }

                    newRow = Math.max(newRow, resultList.size());
                    newColList.add(resultList);
                }

                while(row++ < newRow){
                    list.add(new ArrayList<>());
                    rowMapList.add(new HashMap<>());
                }
                row--;

                // 새로운 열 리스트를 이용하여 행렬 위치에 맞게 값 수정
                // 없는 칸인 경우에는 0 추가
                for(int i = 0; i < row; i++){
                    List<Integer> targetRow = list.get(i);
                    Map<Integer,Integer> rowMap = rowMapList.get(i);
                    rowMap.clear();

                    for(int j = 0; j < col; j++){
                        List<Integer> targetCol = newColList.get(j);
                        int num = targetCol.size() < i + 1 ? 0 : targetCol.get(i);

                        if(targetRow.size() < j + 1) targetRow.add(num);
                        else targetRow.set(j, num);

                        if(num == 0) continue;
                        rowMap.merge(num, 1, Integer::sum);
                    }
                }
            }

            if(R <= row && C <= col && list.get(R - 1).get(C - 1) == K) break;
        }

        if(sec > 100) sec = -1;
        System.out.println(sec);
    }

    private static void sortTarget(List<Integer> target, Map<Integer, Integer> map){
        Collections.sort(target, (o1, o2) -> {
            int v1 = o1 == 0 ? 100 : map.get(o1);
            int v2 = o2 == 0 ? 100 : map.get(o2);
            Integer task = Integer.compare(v1, v2);
            if(task == 0){
                task = Integer.compare(o1, o2);
            }
            return task;
        });
        
    }

    private static void input(int row, int col, List<List<Integer>> list, List<Map<Integer, Integer>> rowMapList){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            R = Integer.parseInt(stk.nextToken());
            C = Integer.parseInt(stk.nextToken());
            K = Integer.parseInt(stk.nextToken());

            for(int i = 0; i < row; i++){
                list.add(new ArrayList<>());
                rowMapList.add(new HashMap<>());

                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < col; j++){
                    int num = Integer.parseInt(stk.nextToken());

                    list.get(i).add(num);
                    rowMapList.get(i).merge(num, 1, Integer::sum);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}