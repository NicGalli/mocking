package com.example;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeManagerTest {

	@InjectMocks
	private EmployeeManager employeeManager;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private BankService bankService;

	@Test
	@DisplayName("Test for payEmployee when employees are not present")
	void test0() {
		given(employeeRepository.findAll()).willReturn(emptyList());
		assertThat(employeeManager.payEmployees()).isZero();
	}

	@Test
	@DisplayName("Test for payEmployee when there is one employee")
	void test1() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 10)));
		assertThat(employeeManager.payEmployees()).isOne();
		verify(bankService).pay("1", 10);
	}

	@Test
	@DisplayName("Test for payEmployee when there are three employees")
	void test2() {
		when(employeeRepository.findAll())
				.thenReturn(asList(new Employee("1", 10), new Employee("3", 15), new Employee("2", 91)));
		assertThat(employeeManager.payEmployees()).isEqualTo(3);
		verify(bankService, times(3)).pay(anyString(), anyDouble());
	}

	@Test
	@DisplayName("Test for payEmployee to activate boolean paid")
	void test3() {
		Employee employee = spy(new Employee("1", 10));
		when(employeeRepository.findAll()).thenReturn(asList(employee));
		assertThat(employeeManager.payEmployees()).isOne();
		InOrder inOrder = inOrder(bankService, employee);
		inOrder.verify(bankService).pay("1", 10);
		inOrder.verify(employee).setPaid(true);
	}

	@Test
	@DisplayName("Test payEmployee when bankService throws exception")
	void test4() {
		Employee employee = spy(new Employee("1", 10));
		when(employeeRepository.findAll()).thenReturn(asList(employee));
		doThrow(new RuntimeException()).when(bankService).pay(anyString(), anyDouble());

		assertThat(employeeManager.payEmployees()).isZero();
		verify(employee).setPaid(false);

	}

	@Test
	@DisplayName("Test other employees are paid when bank service throws exception")
	void test5() {
		Employee employee = spy(new Employee("1", 10));
		Employee employee2 = spy(new Employee("2", 100));

		when(employeeRepository.findAll()).thenReturn(asList(employee, employee2));
		doThrow(new RuntimeException()).doNothing().when(bankService).pay(anyString(), anyDouble());

		assertThat(employeeManager.payEmployees()).isOne();

		verify(employee).setPaid(false);
		verify(employee2).setPaid(true);

	}

}
