package Calculator;

public class ErrorType { // 오류 관련 프로토콜을 저장하는 클래스
    public enum Type{
        TOO_LOW_ARGUMENTS(1, "Too low arguments"), // 변수가 적을 때 오류코드와 메세지
        TOO_MANY_ARGUMENTS(2, "Too many arguments"), // 변수가 많을 때 오류코드와 메세지
        DIVIDED_BY_ZERO(3, "Divided by zero"), // 0으로 나눌 때 오류코드와 메세지
        INVALID_OPERATION(4,"Invalid operation"); // 유효하지 않은 계산일 때 오류코드와 메세지

        private final int code;
        private final String descrption;

        Type(int code, String descrption){ // 생성자
            this.code = code;
            this.descrption = descrption;
        }

        public int getCode(){ // 오류 코드 반환
            return code;
        }

        public String getDescrption(){ // 오류 메세지 반환
            return descrption;
        }

        public static String getDescriptionFromCode(int code){ // 오류 코드를 받으면 그에 해당하는 오류 메세지를 찾아 반환
            for(Type error : values()){
                if(error.code == code){
                    return error.descrption;
                }
            }
            return null;
        }
    }
}
