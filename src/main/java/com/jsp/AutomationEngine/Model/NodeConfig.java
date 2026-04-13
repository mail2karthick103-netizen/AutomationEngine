package com.jsp.AutomationEngine.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.AutomationEngine.Service.ListToNodeConfigConverter1;
import com.jsp.AutomationEngine.Service.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sa_wf_nodeconfig")
public class NodeConfig {
    @Id
    @Column(name = "alt_key")
    private BigInteger altKey=generateAltKey();
    @Column(name = "node_id")
    private String nodeId;
    @Column(name = "node_type")
    private String nodeType;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "node_properties", columnDefinition = "TEXT")
    private Map<String,String> nodeProperties;

    @Column(name = "is_start_node")
    private Boolean isStartNode;
    @Column(name = "is_end_node")
    private Boolean isEndNode;
    @JsonIgnore
    @Convert(converter = ListToNodeConfigConverter1.class)
    @Column(name = "incoming_node")
    private List<NodeConfig> incomingNode;
    @JsonIgnore
    @Convert(converter = ListToNodeConfigConverter1.class)
    @Column(name = "outgoing_node")
    private List<NodeConfig> outgoingNode;

    public BigInteger generateAltKey() {
        return new BigInteger(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) + "");
    }
}
