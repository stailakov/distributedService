package ru.example.server.node;

import ru.example.server.network.Service;
import ru.example.server.network.ServicesProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TaylakovSA
 */
public class Peers {

    private ServicesProperties servicesProps;
    private NodeProperties nodeProperties;
    private final List<Peer> peers = new ArrayList<>();
    private Integer quorum;

    public Peers() {
        servicesProps = new ServicesProperties();
        nodeProperties = new NodePropertiesImpl();
        servicesProps.getServices().stream().
                map(Service::getName).
                map(Integer::parseInt).
                filter(id -> !id.equals(nodeProperties.getId())).
                forEach(this::add);

        quorum = servicesProps.getServices().size()/2+1;
    }


    public Integer getQuorum() {
        return quorum;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    private void add(Integer id) {
        peers.add(new Peer(id));
    }

    public Peer get(Integer id) {
        return peers.stream().
                filter(peer -> peer.getId().equals(id)).
                findFirst().
                orElseThrow(() -> new RuntimeException(String.format("Unsupported peer Id %s",id)));
    }


}
