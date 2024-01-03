package in.example.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.example.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Serializable> {

}
