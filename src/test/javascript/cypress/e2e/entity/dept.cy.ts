import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Dept e2e test', () => {
  const deptPageUrl = '/dept';
  const deptPageUrlPattern = new RegExp('/dept(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const deptSample = {};

  let dept;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/depts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dept) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depts/${dept.id}`,
      }).then(() => {
        dept = undefined;
      });
    }
  });

  it('Depts menu should load Depts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dept');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dept').should('exist');
    cy.url().should('match', deptPageUrlPattern);
  });

  describe('Dept page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deptPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dept page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dept/new$'));
        cy.getEntityCreateUpdateHeading('Dept');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deptPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depts',
          body: deptSample,
        }).then(({ body }) => {
          dept = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dept],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(deptPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dept page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dept');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deptPageUrlPattern);
      });

      it('edit button click should load edit Dept page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dept');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deptPageUrlPattern);
      });

      it('edit button click should load edit Dept page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dept');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deptPageUrlPattern);
      });

      it('last delete button click should delete instance of Dept', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dept').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deptPageUrlPattern);

        dept = undefined;
      });
    });
  });

  describe('new Dept page', () => {
    beforeEach(() => {
      cy.visit(`${deptPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dept');
    });

    it('should create an instance of Dept', () => {
      cy.get(`[data-cy="deptName"]`).type('but');
      cy.get(`[data-cy="deptName"]`).should('have.value', 'but');

      cy.get(`[data-cy="deptAddress"]`).type('beautifully quickly uselessly');
      cy.get(`[data-cy="deptAddress"]`).should('have.value', 'beautifully quickly uselessly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dept = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', deptPageUrlPattern);
    });
  });
});
