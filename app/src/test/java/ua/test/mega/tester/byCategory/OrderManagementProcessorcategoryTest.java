package ua.test.mega.tester.byCategory;


import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.test.mega.tester.byCategory.categories.OrderManagementProcessorCategory;
import ua.test.mega.tester.junit.OrderManagementProcessorJunitTest;
import ua.test.mega.tester.mockito.OrderManagementProcessorMockitoTest;

@RunWith(Categories.class)
@Categories.IncludeCategory(OrderManagementProcessorCategory.class)
@Suite.SuiteClasses({OrderManagementProcessorJunitTest.class, OrderManagementProcessorMockitoTest.class})
public class OrderManagementProcessorcategoryTest {
}
