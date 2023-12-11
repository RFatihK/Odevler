import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Lütfen dosya seçin (1, 2 veya 3):");
        int dosyaSecim = scanner.nextInt();

        String dosyaYolu = null;

        switch (dosyaSecim) {
            case 1:
                dosyaYolu = "src/Data/wl_16_1.txt";
                break;
            case 2:
                dosyaYolu = "src/Data/wl_200_2.txt";
                break;
            case 3:
                dosyaYolu = "src/Data/wl_500_3.txt";
                break;
            default:
                System.out.println("Geçersiz seçim. Program sonlandırılıyor.");
                return;
        }

        List<Customer> customers = new ArrayList<>();
        List<Warehouse> warehouses = new ArrayList<>();
        List<Cost> costs = new ArrayList<>();

        int warehouseCount = 0;
        int customerCount = 0;
        int lineCount = 0;
        int customerId = 1;
        int warehouseId = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String line = br.readLine();

            while (line != null) {
                lineCount++;

                String[] values = line.split(" ");

                if (lineCount == 1) {
                    warehouseCount = Integer.parseInt(values[0]);
                    customerCount = Integer.parseInt(values[1]);
                } else if (lineCount <= warehouseCount + 1) {
                    double capacity = Double.parseDouble(values[0]);
                    double cost = Double.parseDouble(values[1]);

                    Warehouse warehouse = new Warehouse(warehouseId, capacity, cost);
                    warehouses.add(warehouse);

                    warehouseId++;
                } else if (lineCount % 2 == 0) {
                    double demand = Double.parseDouble(values[0]);

                    Customer customer = new Customer(customerId, demand);
                    customers.add(customer);

                    customerId++;
                } else {
                    for (int i = 0; i < values.length; i++) {
                        double value = Double.parseDouble(values[i]);

                        Cost cost = new Cost(customerId - 1, i + 1, value);
                        costs.add(cost);
                    }
                }

                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Dosya okunamadı:");
            System.out.println(e.getMessage());
        }

        costs = costs.stream().sorted(Comparator.comparingDouble(Cost::getValue)).collect(Collectors.toList());

        double totalCost = 0;
        List<Assignment> assignmentList = new ArrayList<>();

        for (Cost cost : costs) {
            if (!assignmentList.stream().anyMatch(a -> a.getCustomerId() == cost.getCustomerId())) {
                Warehouse warehouse = warehouses.stream().filter(w -> w.getId() == cost.getWarehouseId()).findFirst().orElse(null);

                double totalDemand = customers.stream().filter(c -> c.getId() == cost.getCustomerId()).findFirst().orElse(null).getDemand() +
                        assignmentList.stream().filter(a -> a.getWarehouseId() == cost.getWarehouseId())
                                .map(a -> customers.stream().filter(c -> c.getId() == a.getCustomerId()).findFirst().orElse(null).getDemand())
                                .reduce(0.0, Double::sum);
                if (warehouse != null && warehouse.getCapacity() >= totalDemand) {
                    Assignment assignment = new Assignment(cost.getCustomerId(), cost.getWarehouseId(), cost.getValue());
                    assignmentList.add(assignment);
                }
            }
        }

        assignmentList = assignmentList.stream().sorted(Comparator.comparingInt(Assignment::getCustomerId)).collect(Collectors.toList());

        for (Assignment assignment : assignmentList) {
            Customer customer = customers.stream().filter(c -> c.getId() == assignment.getCustomerId()).findFirst().orElse(null);
            Warehouse warehouse = warehouses.stream().filter(w -> w.getId() == assignment.getWarehouseId()).findFirst().orElse(null);

            if (customer != null && warehouse != null) {
                totalCost += assignment.getCost() + warehouse.getCost();
                warehouse.setCapacity(warehouse.getCapacity() - customer.getDemand());
            }
        }

        System.out.println(totalCost);
        for (Assignment assignment : assignmentList) {
            System.out.print((assignment.getWarehouseId() - 1) + " ");
        }
        System.out.println("\n");

        System.out.println(totalCost + " = Optimal Maliyet");
        for (Assignment assignment : assignmentList) {
            System.out.print((assignment.getWarehouseId() - 1) + " ");
        }
        System.out.println("= Müşterilere Atanan depolar ");
        System.out.println();
        for (Assignment assignment : assignmentList) {
            System.out.printf("%d -> %d. Müşteriye atanan depo no%n", assignment.getCustomerId(), assignment.getWarehouseId() - 1);
        }
    }
}

class Customer {
    private int id;
    private double demand;

    public Customer(int id, double demand) {
        this.id = id;
        this.demand = demand;
    }

    public int getId() {
        return id;
    }

    public double getDemand() {
        return demand;
    }
}

class Warehouse {
    private int id;
    private double capacity;
    private double cost;

    public Warehouse(int id, double capacity, double cost) {
        this.id = id;
        this.capacity = capacity;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getCost() {
        return cost;
    }
}

class Cost {
    private int customerId;
    private int warehouseId;
    private double value;

    public Cost(int customerId, int warehouseId, double value) {
        this.customerId = customerId;
        this.warehouseId = warehouseId;
        this.value = value;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public double getValue() {
        return value;
    }
}

class Assignment {
    private int customerId;
    private int warehouseId;
    private double cost;

    public Assignment(int customerId, int warehouseId, double cost) {
        this.customerId = customerId;
        this.warehouseId = warehouseId;
        this.cost = cost;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public double getCost() {
        return cost;
    }
}

