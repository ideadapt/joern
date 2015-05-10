package tests.ES5;

import neo4j.batchInserter.GraphNodeStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ideadapt on 07.05.15.
 */
public class NodeStoreMock extends GraphNodeStore {
    Long id = 0l;
    List<Object> objects = new ArrayList<>();

	@Override
    public void addNeo4jNode(Object o, Map<String, Object> properties) {
        this.objectToId.put(o, ++id);
        objects.add(o);
    }

    public List<Object> getNodes() {
        return objects;
    }

    public Object getNode(Integer idx) {
        return objects.get(idx);
    }
}
