package ua.test.mega.tester.byCategory;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.test.mega.tester.byCategory.categories.OrderProcessorCategory;
import ua.test.mega.tester.junit.OrderProcessorJunitTest;
import ua.test.mega.tester.mockito.OrderProcessorMockitoTest;


@RunWith(Categories.class)
@Categories.IncludeCategory(OrderProcessorCategory.class)
@Suite.SuiteClasses({OrderProcessorJunitTest.class, OrderProcessorMockitoTest.class})
public class OrderProcessorCategoryTest {
}