import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N, TIME;

    private static Map<String, PriorityQueue<Task>> wq_map = new HashMap<>();
    private static Set<String> waiting_url = new HashSet<>();
    
    private static Task[] grader;
    private static Set<String> judged_domains = new HashSet<>();

    private static PriorityQueue<Integer> waiting_grader = new PriorityQueue<>();

    private static Map<String, Task> history = new HashMap<>(); // 도메인, Task

    private static class Task{
        int input, start, end;
        int priority;
        String url;
        Task(int input, int priority, String url){
            this.input = input;
            this.priority = priority;
            this.url = url;
            this.start = -1;
            this.end = -1;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            
            String command = stk.nextToken();
            if("100".equals(command)){
                N = Integer.parseInt(stk.nextToken());
                grader = new Task[N + 1];
                for(int i = 1; i <= N; i++){
                    waiting_grader.add(i);
                }

                String url = stk.nextToken();
                Task task = new Task(0, 1, url);

                String domain = url.split("/")[0];

                PriorityQueue<Task> pq = new PriorityQueue<>((o1, o2)->{
                    if(o1.priority == o2.priority) return o1.input - o2.input;
                    return o1.priority - o2.priority;
                });
                pq.add(task);
                waiting_url.add(url);

                wq_map.put(domain, pq);
            }else if("200".equals(command)){ // 채점 데이터 추가
                TIME = Integer.parseInt(stk.nextToken());
                int p = Integer.parseInt(stk.nextToken());
                String url = stk.nextToken();

                if(waiting_url.contains(url)) continue;
                Task task = new Task(TIME, p, url);

                String domain = url.split("/")[0];
                PriorityQueue<Task> pq = wq_map.containsKey(domain) ? wq_map.get(domain) : new PriorityQueue<>((o1, o2)->{
                    if(o1.priority == o2.priority) return o1.input - o2.input;
                    return o1.priority - o2.priority;
                });

                pq.add(task);
                waiting_url.add(url);

                wq_map.put(domain, pq);
            }else if("300".equals(command)){ // 채점 시작 시간
                TIME = Integer.parseInt(stk.nextToken());

                if(waiting_grader.isEmpty()) continue;

                Task task = null;
                for(String domain : wq_map.keySet()){
                    PriorityQueue<Task> pq = wq_map.get(domain);
                    if(!statusCheck(domain) || pq.isEmpty()) continue;
                    Task tmp = pq.poll();
                    pq.add(tmp);

                    if(task == null || task.priority > tmp.priority || (task.priority == tmp.priority && task.input > tmp.input)){
                        task = tmp;
                    }
                }

                if(task == null) continue;
                task.start = TIME;

                String domain = task.url.split("/")[0];
                wq_map.get(domain).poll();

                int j_id = waiting_grader.poll();
                grader[j_id] = task;
                judged_domains.add(task.url.split("/")[0]);
                waiting_url.remove(task.url);
            }else if("400".equals(command)){ // 채점 종료 시간
                TIME = Integer.parseInt(stk.nextToken());
                int j_id = Integer.parseInt(stk.nextToken());

                Task task = grader[j_id];
                if(task == null) continue;
                grader[j_id] = null;

                task.end = TIME;

                String domain = task.url.split("/")[0];
                judged_domains.remove(domain);
                history.put(domain, task);
                waiting_grader.add(j_id);
            }else if("500".equals(command)){
                TIME = Integer.parseInt(stk.nextToken());
                int sum = 0;

                for(String domain : wq_map.keySet()){
                    sum += wq_map.get(domain).size();
                }
                
                sb.append(sum).append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    private static boolean statusCheck(String domain){
        if(judged_domains.contains(domain)) return false;
        if(history.containsKey(domain)){
            Task value = history.get(domain);
            int gap = value.end - value.start;
            return TIME >= value.start + 3 * gap;
        }
        return true;
    }
}