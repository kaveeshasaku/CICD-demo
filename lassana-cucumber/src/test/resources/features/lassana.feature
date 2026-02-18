Feature: Lassana Home Website Testing

  Scenario: Open Lassana website and verify title
    Given user opens the browser
    When user navigates to "https://www.lassana.com"
    Then page title should contain "Lassana"

  Scenario: Search product and add to cart
    Given user opens the browser
    When user navigates to "https://www.lassana.com"
    And user searches for "cake"
    And user selects the second product
    And user adds product to cart
    Then user goes to cart and proceeds to checkout
