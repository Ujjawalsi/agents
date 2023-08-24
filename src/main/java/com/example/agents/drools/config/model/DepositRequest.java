package com.example.agents.drools.config.model;

public class DepositRequest {
    private String loanType;
    private Integer durationInYear;
    private Integer age;
    private String interestRate;

    public DepositRequest(String loanType, Integer durationInYear, Integer age) {
        super();
        this.loanType = loanType;
        this.durationInYear = durationInYear;
        this.age = age;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Integer getDurationInYear() {
        return durationInYear;
    }

    public void setDurationInYear(Integer durationInYear) {
        this.durationInYear = durationInYear;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

}
