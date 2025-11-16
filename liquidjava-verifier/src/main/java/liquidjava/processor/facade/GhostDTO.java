package liquidjava.processor.facade;

import java.util.List;

public record GhostDTO(String name, List<String> param_types, String return_type) {
}
