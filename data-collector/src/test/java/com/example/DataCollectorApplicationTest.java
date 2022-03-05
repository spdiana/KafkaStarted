package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = DataCollectorApplication.class)
public class DataCollectorApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String TOPIC = "demo-data-topic";

    private static KafkaContainer kafka;

    static  {
        final Network commonNetwork = Network.newNetwork();
        setZookeeperAndKafka(commonNetwork);
        createTopic(TOPIC);
    }

    private static void setZookeeperAndKafka(Network network) {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
                .withNetwork(network);
        kafka.start();
    }


    @Test
    public void check_kafka_status() {
        assertThat(kafka.isCreated()).isTrue();
        assertThat(kafka.isRunning()).isTrue();
    }


    @Test
    public void send_message_success() throws Exception {
        mockMvc.perform(post("/read")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"first_name\": \"tre\",\n" +
                                "\"last_name\": \"test1\",\n" +
                                "\"age\": 67\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static void createTopic(String topicName) {
        // kafka container uses with embedded zookeeper
        // confluent platform and Kafka compatibility 5.1.x <-> kafka 2.1.x
        // kafka 2.1.x require option --zookeeper, later versions use --bootstrap-servers instead
        String createTopic =
                String.format(
                        "/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic %s",
                        topicName);
        try {
            final Container.ExecResult execResult = kafka.execInContainer("/bin/sh", "-c", createTopic);
            if (execResult.getExitCode() != 0) fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
