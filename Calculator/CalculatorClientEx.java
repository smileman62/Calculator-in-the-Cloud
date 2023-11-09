package Calculator;

import Calculator.ErrorType.Type;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalculatorClientEx {
    public static void main(String[] args) {
        String serverIP = "localHost";
        int port = 9999;
        File configFile = new File ("server_info.dat");
        //서버 정보가 담긴 파일이 존재하면, 파일 사용, 아니면 default값 사용

        if (configFile.exists()){ // 파일이 존재할 경우, 파일서 값을 불러오기 시도.
            try (Scanner scanner = new Scanner(configFile)){
                serverIP = scanner.nextLine();
                port = Integer.parseInt(scanner.nextLine());
            } catch(Exception e){
                System.out.println("서버 정보를 불러오는데 실패했습니다. 기본 정보를 불러옵니다.");
            }
        }
        else{ // 파일이 존재하지 않을 경우 기본 정보로 연결
            System.out.println("configFile을 찾을 수 없습니다. 기본 정보를 불러옵니다.");
        }

        try { // 연결
            Socket socket = new Socket(serverIP,port);
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 새 입력 버퍼 할당
            var out = new PrintWriter(socket.getOutputStream(),true); // 새 쓰기 버퍼 할당
            var scanner = new Scanner(System.in);

            System.out.println("서버와 연결되었습니다. " + serverIP + " : " + port);
            System.out.println("지원 연산자 : 더하기 (+), 뺴기 (-), 곱하기 (*), 나누기 (/)");
            System.out.println("A 연산자 B로 입력해주세요. (빈칸 띄어쓰기 유의), 예시 : 24 + 42");

            while (true){
                System.out.print(">> ");
                String input = scanner.nextLine(); // 입력 값을 받음

                if("exit".equalsIgnoreCase(input)){ // "exit"를 입력 받을 경우 while문 종료
                    break;
                }

                out.println(input); // 입력한 값 출력
                String response = in.readLine(); // 서버에서 받은 값을 할당

                if(response.startsWith("Error: ")){ // 만약 받은 값이 "Error: " 로 시작할 경우,,,
                    String errorCodeStr = response.substring("Error: ".length()); // 받은 값에서 오류 코드 추출
                    try{
                        int errorCode = Integer.parseInt(errorCodeStr); // 오류 코드를 int값으로 변환
                        System.out.println("Error code: " + errorCode +", " + Type.getDescriptionFromCode(errorCode)); // 오류 코드를 활용해 오류 코드와 오류 메세지 출력
                    } catch (NumberFormatException e){
                        System.out.println("서버가 유효하지 않은 오류 코드를 보냈습니다.");
                    }
                }
                else{ // 그 외의 경우 답을 출력함.
                    System.out.println("정답 : " + response);
                }
            }
        } catch(Exception e){
            System.out.println("오류 : " + e.getMessage());
        }
    }
}
