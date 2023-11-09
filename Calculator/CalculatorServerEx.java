package Calculator;

import Calculator.ErrorType.Type;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorServerEx {

    public static String calc(String exp) { // 계산 기능이 담긴 클래스
        StringTokenizer st = new StringTokenizer(exp, " "); // 공백을 기준으로 토큰을 나눔

        if(st.countTokens() < 3){ // 토큰의 개수가 3개 보다 적을 때 오류 코드 반환
            return ("Error: " + Type.TOO_LOW_ARGUMENTS.getCode());
        }

        else if(st.countTokens() > 3){ // 토큰의 개수가 3개 보다 많을 때 오류 코드 반환
            return ("Error: " + Type.TOO_MANY_ARGUMENTS.getCode());
        }

        String res = ""; // 결과값 초기화
        int op1 = Integer.parseInt(st.nextToken()); // 첫번쨰 변수
        String opcode = st.nextToken(); // 연산자 저장
        int op2 = Integer.parseInt(st.nextToken()); // 두번째 변수

        switch (opcode) { // 연산자의 경우에 따라 각기 다른 결과 값을 반환
            case "+":
                res = Integer.toString(op1 + op2);
                break;
            case "-":
                res = Integer.toString(op1 - op2);
                break;
            case "*":
                res = Integer.toString(op1 * op2);
                break;
            case "/":
                if(op2 == 0){ // 만일 연산자가 "/"이고 두번째 변수가 0일 경우 0으로 나누는 상황이므로 오류코드 반환
                    return ("Error: " +Type.DIVIDED_BY_ZERO.getCode());
                }
                else {
                    res = Integer.toString(op1 / op2);
                    break;
                }
            default: // 그 외의 경우 유효하지 않은 연산이라 판단하여 오류코드 반환
                return ("Error: " + Type.INVALID_OPERATION.getCode());
        }
        return res; // 결과값 반환
    }

    public static void main(String[] args) throws Exception {
        int port = 9999;
        ServerSocket listner = new ServerSocket(port); // 서버 소켓 생성
        System.out.println("서버가 구동 중입니다. port number : " + port);
        ExecutorService pool = Executors.newFixedThreadPool(100); // 쓰레드 생성 갯수 100개로 제한

        while(true){
            Socket sock = listner.accept(); // 클라이언트 입력까지 대기 후 연결
            pool.execute(new CalcServerEx(sock)); // 새 쓰레드 생성
        }
    }

    private static class CalcServerEx implements Runnable {
        private Socket socket;

        CalcServerEx(Socket socket){
           this.socket = socket; // 서버 소켓을 받아옴
        }

        @Override
        public void run() {
            System.out.println("연결되었습니다.");

            BufferedReader in = null; // 입력 버퍼 초기화
            BufferedWriter out = null; // 쓰기 버퍼 초기화
            Socket socket = this.socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 입력 버퍼 받아오기
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 출력 버퍼 받아오기

                while (true) { // 연결이 끊기기 전까지 아래의 코드 반복
                    String inputMessage = in.readLine(); // 클라이언트에게서 입력 값 받아오기
                    if (inputMessage.equalsIgnoreCase("exit")) { // 만약 입력 값이 "exit"일 경우 연결 종료
                        System.out.println("클라이언트에서 연결을 종료하였음");
                        break; //  연결 종료
                    }
                    System.out.println(inputMessage); // 서버가 받은 메시지를 화면에 출력
                    String res = calc(inputMessage); // 계산. 계산 결과는 res
                    out.write(res + "\n"); // 계산 결과 문자열 전송
                    out.flush();
                }
            } catch (IOException e) {
               System.out.println(e.getMessage());
            } finally {
                try {
                    if (socket != null)
                        socket.close(); // 통신용 소켓 닫기

                }   catch (IOException e) {
                    System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
                }
            }
        }
    }
}
