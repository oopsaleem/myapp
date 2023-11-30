<template>
  <div>
    <h2 id="page-heading" data-cy="EmpHeading">
      <span id="emp-heading">Emps</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'EmpCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-emp">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Emp</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && emps && emps.length === 0">
      <span>No Emps found</span>
    </div>
    <div class="table-responsive" v-if="emps && emps.length > 0">
      <table class="table table-striped" aria-describedby="emps">
        <thead>
          <tr>
            <th scope="row"><span>Translation missing for global.field.id</span></th>
            <th scope="row"><span>First Name</span></th>
            <th scope="row"><span>Last Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="emp in emps" :key="emp.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EmpView', params: { empId: emp.id } }">{{ emp.id }}</router-link>
            </td>
            <td>{{ emp.firstName }}</td>
            <td>{{ emp.lastName }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EmpView', params: { empId: emp.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Translation missing for entity.action.view</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EmpEdit', params: { empId: emp.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Translation missing for entity.action.edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(emp)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Translation missing for entity.action.delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="myappApp.emp.delete.question" data-cy="empDeleteDialogHeading">Translation missing for entity.delete.title</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-emp-heading">Are you sure you want to delete Emp {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Translation missing for entity.action.cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-emp"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeEmp()"
          >
            Translation missing for entity.action.delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./emp.component.ts"></script>
