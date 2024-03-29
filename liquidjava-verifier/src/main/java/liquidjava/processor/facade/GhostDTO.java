package liquidjava.processor.facade;

import java.util.List;

public class GhostDTO {

    private String name;
    private List<String> param_types;
    private String return_type;

    public GhostDTO(String name, List<String> param_types, String return_type) {
        super();
        this.name = name;
        this.param_types = param_types;
        this.return_type = return_type;
    }

    public String getName() {
        return name;
    }

    public List<String> getParam_types() {
        return param_types;
    }

    public String getReturn_type() {
        return return_type;
    }
}
