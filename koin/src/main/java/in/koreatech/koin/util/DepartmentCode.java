package in.koreatech.koin.util;

public enum DepartmentCode {
    DEPARTMENT_CODE_0(0, "전체"),
    DEPARTMENT_CODE_1(1, "컴퓨"),
    DEPARTMENT_CODE_2(2, "기계"),
    DEPARTMENT_CODE_3(3, "전기"),
    DEPARTMENT_CODE_4(4, "에너지"),
    DEPARTMENT_CODE_5(5, "산업"),
    DEPARTMENT_CODE_6(6, "메카"),
    DEPARTMENT_CODE_7(7, "디자인"),
    DEPARTMENT_CODE_8(8, "교양"),
    DEPARTMENT_CODE_9(9, "HRD"),
    DEPARTMENT_CODE_10(10, "융합");


    private int type;
    private String departmentString;

    DepartmentCode(int type, String departmentString) {
        this.departmentString = departmentString;
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public String getDepartMentString() {
        return departmentString;
    }
}
