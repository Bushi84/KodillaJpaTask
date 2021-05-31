package com.kodilla.jpatask.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TaskTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private List<Long> insertExampleData() {
        Person p1 = new Person(null, "name1", "surname1");
        Person p2 = new Person(null, "name2", "surname2");
        Person p3 = new Person(null, "name3", "surname3");

        Task t1 = new Task(null, "task1", Status.READY);
        Task t2 = new Task(null, "task2", Status.READY);

        SubTask st11 = new SubTask(null, "subtask11", Status.READY, t1);
        SubTask st12 = new SubTask(null, "subtask12", Status.READY, t1);
        t1.getSubTasks().addAll(List.of(st11, st12));

        SubTask st21 = new SubTask(null, "subtask21", Status.READY, t2);
        SubTask st22 = new SubTask(null, "subtask22", Status.READY, t2);
        t2.getSubTasks().addAll(List.of(st21, st22));

        List<Person> resp_1 = new ArrayList<>();
        resp_1.add(p1);
        List<Person> resp_11 = new ArrayList<>();
        resp_11.add(p2);
        List<Person> resp_12 = new ArrayList<>();
        resp_12.add(p3);

        List<Person> resp_2 = new ArrayList<>();
        resp_2.addAll(List.of(p3, p2));
        List<Person> resp_21 = new ArrayList<>();
        resp_21.addAll(List.of(p2, p1));
        List<Person> resp_22 = new ArrayList<>();
        resp_21.addAll(List.of(p1, p3));

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        em.persist(t1);
        em.persist(t2);

        em.persist(st11);
        em.persist(st12);
        em.persist(st21);
        em.persist(st22);

        em.flush();
        em.getTransaction().commit();
        em.close();

        return List.of(t1.getId(), t2.getId());
    }

    @Test
    void shouldNPlusOneProblemOccure() {
        //Given
        List<Long> savedTasks = insertExampleData();
        EntityManager em = emf.createEntityManager();

        //When
        System.out.println("****************** BEGIN OF FETCHING *******************");
        System.out.println("*** STEP 1 – query for tasks ***");

        TypedQuery<Task> query =
                em.createQuery(
                        "from Task "
                                + " where id in (" + taskIds(savedTasks) + ")",
                        Task.class);

        EntityGraph<Task> eg = em.createEntityGraph(Task.class);
        eg.addAttributeNodes("responsible");
        eg.addSubgraph("subTasks").addAttributeNodes("responsible");;
        query.setHint("javax.persistence.fetchgraph", eg);

        List<Task> tasks =
                query.getResultList();

        for (Task task : tasks) {
            System.out.println("*** STEP 2 – read data from task ***");
            System.out.println(task);

            System.out.println("*** STEP 3 – read list of responsible ***");
            for (Person person : task.getResponsible()) {
                System.out.println(person);
            }

            System.out.println("*** STEP 4 – read list of subtasks ***");
            for (SubTask subTask : task.getSubTasks()) {
                System.out.println(subTask);

                System.out.println("*** STEP 5 – read list of responsible for subtask***");
                for (Person person : subTask.getResponsible()) {
                    System.out.println(person);
                }
            }
        }

        System.out.println("****************** END OF FETCHING *******************");
    }

    private String taskIds(List<Long> taskeIds) {
        return taskeIds.stream()
                .map(n -> "" + n)
                .collect(Collectors.joining(","));
    }
}
