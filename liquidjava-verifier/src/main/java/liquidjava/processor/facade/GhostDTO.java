package liquidjava.processor.facade;

import java.util.List;

public record GhostDTO(String name, List<String> paramTypes, String returnType) {
}
