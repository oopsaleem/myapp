<template>
  <div>
    <h2 id="page-heading" data-cy="DeptHeading">
      <span id="dept-heading">Depts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'DeptCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-dept">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Dept</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && depts && depts.length === 0">
      <span>No Depts found</span>
    </div>
    <div class="table-responsive" v-if="depts && depts.length > 0">
      <table class="table table-striped" aria-describedby="depts">
        <thead>
          <tr>
            <th scope="row"><span>Translation missing for global.field.id</span></th>
            <th scope="row"><span>Dept Name</span></th>
            <th scope="row"><span>Dept Address</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dept in depts" :key="dept.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DeptView', params: { deptId: dept.id } }">{{ dept.id }}</router-link>
            </td>
            <td>{{ dept.deptName }}</td>
            <td>{{ dept.deptAddress }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DeptView', params: { deptId: dept.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Translation missing for entity.action.view</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DeptEdit', params: { deptId: dept.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Translation missing for entity.action.edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dept)"
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
        <span id="myappApp.dept.delete.question" data-cy="deptDeleteDialogHeading">Translation missing for entity.delete.title</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dept-heading">Are you sure you want to delete Dept {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Translation missing for entity.action.cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-dept"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeDept()"
          >
            Translation missing for entity.action.delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./dept.component.ts"></script>
