package com.ivory.ivory.domain;

public enum Disease {
    // 유행성 질병
    COLD("감기"),
    CONJUNCTIVITIS("눈병"),
    STOMATITIS("구내염"),
    TRANSMISSIBLE("전염"), // 전염 여부는 별도 로직으로 처리 가능

    // 제1급 감염병
    EBOLA("에볼라"),
    MARBURG_FEVER("마버그열"),
    LASSA_FEVER("라싸열"),
    CRIMEAN_CONGO_HEMORRHAGIC_FEVER("크리미안콩고출혈열"),
    SOUTH_AMERICAN_HEMORRHAGIC_FEVER("남아메리카출혈열"),
    RIFT_VALLEY_FEVER("리프트밸리열"),
    SMALLPOX("두창"),
    PLAGUE("페스트"),
    ANTHRAX("탄저"),
    BOTULISM("보툴리눔독소증"),
    TULAREMIA("야토병"),
    SARS("SARS"),
    MERS("MERS"),
    H5N1("동물인플루엔자인체감염증"),
    H1N1("신종인플루엔자"),
    DIPHTHERIA("디프테리아"),

    // 제2급 감염병
    TUBERCULOSIS("결핵"),
    CHICKENPOX("수두"),
    MEASLES("홍역"),
    CHOLERA("콜레라"),
    TYPHOID_FEVER("장티푸스"),
    PARATYPHOID_FEVER("파라티푸스"),
    SHIGELLOSIS("세균성이질"),
    EHEC("장출혈성대장균"),
    HEPATITIS_A("A형간염"),
    // 생략된 다른 제2급 감염병...

    // 제3급 감염병
    TETANUS("파상풍"),
    HEPATITIS_B("B형간염"),
    HEPATITIS_C("C형간염"),
    JAPANESE_ENCEPHALITIS("일본뇌염"),
    MALARIA("말라리아"),
    // 생략된 다른 제3급 감염병...

    // 제4급 감염병
    INFLUENZA("인플루엔자"),
    HAND_FOOT_MOUTH_DISEASE("수족구병"),
    PARASITIC_INFECTION("기생충 감염"),
    GONORRHEA("임질"),
    CHLAMYDIA("클라미디아"),
    COVID_19("코로나19");

    private final String name;

    Disease(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Disease findByDiagnosisName(String diagnosisName) {
        if (diagnosisName == null || diagnosisName.isEmpty()) {
            throw new IllegalArgumentException("진단명이 비어있습니다.");
        }

        for (Disease disease : Disease.values()) {
            if (diagnosisName.contains(disease.getName())) {
                return disease;
            }
        }
        throw new IllegalArgumentException("적합하지 않은 질병명입니다: " + diagnosisName);
    }
}