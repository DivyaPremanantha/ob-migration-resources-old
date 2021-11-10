package org.wso2.carbon.ob.migration.util;

public enum Specification {

    UK("UK"),
    AU("AU"),
    BG("BG");

    private String specification;

    private Specification(String specification) {

        this.specification = specification;
    }

    public String getSpecification() {

        return specification;
    }
}
