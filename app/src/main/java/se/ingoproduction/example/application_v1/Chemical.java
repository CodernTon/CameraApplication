package se.ingoproduction.example.application_v1;

public class Chemical {
    private String substance;
    private String casnr;
    private String egnr;
    private String priolevel;
    private String criteria;

    public Chemical(String substance, String casnr,String egnr, String priolevel, String criteria) {

        this.substance=substance;
        this.casnr = casnr;
        this.egnr = egnr;
        this.priolevel = priolevel;
        this.criteria=criteria;
    }
    public Chemical(){}

    public String getSubstance() {
        return substance;
    }

    public String getCasnr() {
        return casnr;
    }

    public String getEgnr() {
        return egnr;
    }

    public String getPriolevel() {
        return priolevel;
    }

    public String getCriteria() {
        return criteria;
    }
    @Override
    public String toString() {
        return "\n"+"Substance: " + this.substance + "\n" +
                "Criteria: " + this.criteria + "\n" +
                "CAS number: " + this.casnr + "\n" +
                "EG number: " + this.egnr + "\n" +
                "Priority level: " + this.priolevel + "\n" +
                "----------" + "\n";
    }
}

