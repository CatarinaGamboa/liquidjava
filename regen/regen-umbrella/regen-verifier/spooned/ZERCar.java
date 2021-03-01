

/**
 * Car allowed to drive in ZER zones (Zona de Emissões Reduzidas)
 * Cars with license plate before 2005 are prohibited from driving in this areas
 *
 * @author Catarina Gamboa
 */
public class ZERCar extends Car {
    @java.lang.Override
    public void setYear(@repair.regen.specification.Refinement("_ >= 2005")
    int year) {
        // error if the upper limit is not set
        super.setYear(year);
    }
}

