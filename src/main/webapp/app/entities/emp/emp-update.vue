<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="myappApp.emp.home.createOrEditLabel" data-cy="EmpCreateUpdateHeading">Create or edit a Emp</h2>
        <div>
          <div class="form-group" v-if="emp.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="emp.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="emp-firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="emp-firstName"
              data-cy="firstName"
              :class="{ valid: !v$.firstName.$invalid, invalid: v$.firstName.$invalid }"
              v-model="v$.firstName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="emp-lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="emp-lastName"
              data-cy="lastName"
              :class="{ valid: !v$.lastName.$invalid, invalid: v$.lastName.$invalid }"
              v-model="v$.lastName.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./emp-update.component.ts"></script>
