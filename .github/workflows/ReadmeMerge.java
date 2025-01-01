import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class ReadmeMerge {
    private static final String rootPath = ".";

    private static final StringBuilder header = new StringBuilder()
            .append("# \uD83D\uDCD6 학습하기\n")
            .append("\n")
            .append("# \uD83E\uDD47 실전 훈련\n")
            .append("|기록분류|이름|티어|유형|상태|최근 제출 코드|\n")
            .append("|---|---|---|---|---|---|\n");

    private static final StringBuilder footer = new StringBuilder();

    public static void main(String[] args) {
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rootPath + "/README.md")))){
            writer.write(header.toString());
            Files.walk(Paths.get(rootPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> "README.md".equalsIgnoreCase(path.getFileName().toString()))
                    .filter(path -> !path.toAbsolutePath().toString().equals(Paths.get(rootPath).toAbsolutePath().toString()))
                    .forEach(path -> {
                        try(BufferedReader br = Files.newBufferedReader(path)){
                            while(br.ready()){
                                String line = br.readLine();
                                if(line.contains("기록분류")){
                                    br.readLine(); // 테이블 구분선 필터링
                                    String str = br.readLine();
                                    while(str != null && !"".equals(str)){
                                        writer.write(str + "\n");
                                        str = br.readLine();
                                    }

                                    if(footer.isEmpty()){
                                        while(br.ready()){
                                            str = br.readLine();
                                            footer.append(str).append("\n");
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            writer.write(footer.toString());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
